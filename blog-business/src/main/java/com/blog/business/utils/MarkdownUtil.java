package com.blog.business.utils;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 * Markdown工具类
 *
 * @author 31373
 */
public class MarkdownUtil {

    private static final Parser PARSER = Parser.builder().build();
    private static final HtmlRenderer RENDERER = HtmlRenderer.builder().build();

    // 中文阅读速度：约 300-500 字/分钟（取平均值 400）
    private static final int CHINESE_READING_SPEED = 400;
    // 英文阅读速度：约 200-250 词/分钟（取平均值 225）
    private static final int ENGLISH_READING_SPEED = 225;
    //中文字符
    private static final String CHINESE_CHARACTERS = "[\\u4e00-\\u9fff]";
    //字母的单词（排除纯数字和符号）
    private static final String ENGLISH_WORDS = ".*[a-zA-Z].*";
    //空格和标点
    private static final String PUNCTUATION = "[\\s+]";


    /**
     * Markdown转HTML（带XSS防护）
     *
     * @param markdown Markdown文本
     * @return 安全的HTML文本
     */
    public static String toHtml(String markdown) {
        if (markdown == null || markdown.trim().isEmpty()) {
            return "";
        }

        // 解析Markdown
        Node document = PARSER.parse(markdown);
        String html = RENDERER.render(document);

        // 安全过滤（防止XSS）
        return sanitizeHtml(html);
    }

    /**
     * 判断是否是 HTML 内容（如富文本编辑器输出）
     */
    public static boolean isHtml(String content) {
        if (content == null) {
            return false;
        }
        String trimmed = content.trim();
        return trimmed.startsWith("<");
    }

    /**
     * 对 HTML 内容进行 XSS 安全过滤（不进行 Markdown 转换）
     */
    public static String sanitize(String html) {
        if (html == null || html.trim().isEmpty()) {
            return "";
        }
        return sanitizeHtml(html);
    }

    /**
     * 统计 Markdown 内容的字数
     *
     * @param markdown Markdown 文本
     * @return 字数（中文按字符数，英文按单词数）
     */
    public static int countWords(String markdown) {
        if (markdown == null || markdown.trim().isEmpty()) {
            return 0;
        }
        // 转换为纯文本（去除 Markdown 语法和 HTML 标签）
        String plainText = extractPlainText(markdown);
        // 统计中文字符数
        int chineseCount = countChineseCharacters(plainText);
        // 统计英文单词数
        int englishWordCount = countEnglishWords(plainText);

        // 返回总字数（中文 + 英文）
        return chineseCount + englishWordCount;
    }

    /**
     * 计算预计阅读时间（分钟）
     *
     * @param markdown Markdown 文本
     * @return 预计阅读时间（分钟）
     */
    public static int calculateReadingTime(String markdown) {
        int wordCount = countWords(markdown);
        if (wordCount == 0) {
            return 0;
        }

        // 判断中英文比例
        String plainText = extractPlainText(markdown);
        int chineseCount = countChineseCharacters(plainText);
        int englishWordCount = countEnglishWords(plainText);

        // 如果中文占主导（超过 50%），使用中文阅读速度
        if (chineseCount > englishWordCount) {
            return (int) Math.ceil((double) wordCount / CHINESE_READING_SPEED);
        } else {
            // 否则使用英文阅读速度
            return (int) Math.ceil((double) wordCount / ENGLISH_READING_SPEED);
        }
    }

    /**
     * 从 Markdown 或 HTML 中提取纯文本
     */
    private static String extractPlainText(String content) {
        if (content == null || content.trim().isEmpty()) {
            return "";
        }
        // 如果是 HTML，先用 Jsoup 提取纯文本
        if (isHtml(content)) {
            return Jsoup.parse(content).text();
        }
        // 如果是 Markdown，先转 HTML 再提取纯文本
        Node document = PARSER.parse(content);
        String html = RENDERER.render(document);
        return Jsoup.parse(html).text();
    }

    /**
     * 统计中文字符数（包括中文标点）
     */
    private static int countChineseCharacters(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (char c : text.toCharArray()) {
            // 判断是否为中文字符（Unicode 范围）
            if (c >= '\u4e00' && c <= '\u9fff') {
                count++;
            }
        }
        return count;
    }

    /**
     * 统计英文单词数
     */
    private static int countEnglishWords(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        // 移除中文字符
        String englishOnly = text.replaceAll(CHINESE_CHARACTERS, " ");
        // 按空格和标点分割单词
        String[] words = englishOnly.trim().split(PUNCTUATION);
        int count = 0;
        for (String word : words) {
            // 只统计包含字母的单词（排除纯数字和符号）
            if (!word.isEmpty() && word.matches(ENGLISH_WORDS)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 安全的 HTML 过滤
     */
    private static String sanitizeHtml(String html) {
        Safelist safelist = Safelist.relaxed()
                // 允许的标签
                .addTags("code", "pre", "hr")
                // 允许code标签的class属性（用于语法高亮）
                .addAttributes("code", "class")
                // 只允许安全协议
                .addProtocols("a", "href", "http", "https", "mailto")
                // 允许target和rel属性
                .addAttributes("a", "target", "rel")
                // 自动加安全属性
                .addEnforcedAttribute("a", "rel", "nofollow noopener noreferrer");

        // 清理HTML
        return Jsoup.clean(html, safelist);
    }
}
