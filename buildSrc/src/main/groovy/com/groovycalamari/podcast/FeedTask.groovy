package com.groovycalamari.podcast

import groovy.transform.CompileStatic
import io.micronaut.rss.RssChannelImage
import io.micronaut.rss.RssFeedRenderer
import io.micronaut.rss.RssItemEnclosure
import io.micronaut.rss.RssLanguage
import io.micronaut.rss.itunespodcast.DefaultItunesPodcastRenderer
import io.micronaut.rss.itunespodcast.ItunesPodcast
import io.micronaut.rss.itunespodcast.ItunesPodcastCategory
import io.micronaut.rss.itunespodcast.ItunesPodcastEpisode
import io.micronaut.rss.itunespodcast.ItunesPodcastOwner
import io.micronaut.rss.itunespodcast.ItunesPodcastType
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@CompileStatic
class FeedTask extends DefaultTask {
    static final String COLON = ":"
    static final String SEPARATOR = "---"
    public static final String SUFFIX_MD = ".md"
    public static final String SUFFIX_MARKDOWN = ".markdown"
    public static final String RSS_FILE = 'rss.xml'
    public static final String COMMA = ","

    @Input
    final Property<String> title = project.objects.property(String)

    @Input
    final Property<String> link = project.objects.property(String)

    @Input
    final Property<String> copyright = project.objects.property(String)

    @Input
    final Property<String> language = project.objects.property(String)

    @Input
    final Property<String> keywords = project.objects.property(String)

    @Input
    final Property<String> podcastDescription = project.objects.property(String)

    @Input
    final Property<String> subtitle = project.objects.property(String)

    @Input
    final Property<String> author = project.objects.property(String)

    @Input
    final Property<String> type = project.objects.property(String)

    @Input
    final Property<String> ownerName = project.objects.property(String)

    @Input
    final Property<String> ownerEmail = project.objects.property(String)

    @Input
    final Property<String> imageUrl = project.objects.property(String)

    @Input
    final Property<String> imageLink = project.objects.property(String)

    @Input
    final Property<Boolean> block = project.objects.property(Boolean)

    @Input
    final Property<Boolean> explicit = project.objects.property(Boolean)

    @Input
    final Property<String> categories = project.objects.property(String)

    @InputDirectory
    final Property<File> episodes = project.objects.property(File)

    @OutputDirectory
    final Property<File> output = project.objects.property(File)

    @TaskAction
    void renderFeed() {
        ItunesPodcast.Builder podcastBuilder = ItunesPodcast.builder()
        ZonedDateTime d = ZonedDateTime.of(LocalDateTime.of(2018, 12, 18, 20, 0, 0), ZoneId.of("GMT"))
        println DateTimeFormatter.ISO_DATE_TIME.format(d);

        podcastBuilder.title(title.get())
        podcastBuilder.link(link.get())
        podcastBuilder.link(copyright.get())
        Optional<RssLanguage> optionalLang = RssLanguage.LANG_ENGLISH_UNITED_STATES.of(language.get())
        if (optionalLang.isPresent()) {
           podcastBuilder.language(optionalLang.get())
        }

        for (String keyword : keywords.get().split(COMMA)) {
            podcastBuilder.keyword(keyword)
        }
        podcastBuilder.description(podcastDescription.get())
        podcastBuilder.subtitle(subtitle.get())
        podcastBuilder.author(author.get())
        podcastBuilder.type(type.get() as ItunesPodcastType)
        ItunesPodcastOwner.Builder ownerBuilder = ItunesPodcastOwner.builder()
        ownerBuilder.name(ownerName.get())
        ownerBuilder.name(ownerEmail.get())
        podcastBuilder.owner(ownerBuilder.build())
        RssChannelImage.Builder imageBuilder = RssChannelImage.builder(title.get(),imageUrl.get(), imageLink.get())
        podcastBuilder.image(imageBuilder.build())
        podcastBuilder.block(block.get())
        podcastBuilder.explict(explicit.get())
        podcastBuilder.category([(categories.get() as ItunesPodcastCategory).getCategories()])

        List<MarkdownEpisode> episodes = parseEpisodes(episodes.get())
        episodes.each {
            ItunesPodcastEpisode.Builder builder = ItunesPodcastEpisode.builder(it.title)
            if (it.episodeType) {
                builder.episodeType(it.episodeType)
            }
            if (it.guid) {
                builder.guid(it.guid)
            }
            if (it.duration) {
                builder.duration(it.duration)
            }
            if (it.image) {
                builder.image(it.image)
            }
            if (it.episode) {
                builder.episode(it.episode)
            }
            if (it.season) {
                builder.season(it.season)
            }
            if (it.explicit) {
                builder.explicit(it.explicit)
            }
            if (it.author) {
                builder.author(it.author)
            }
            if (it.subtitle) {
                builder.subtitle(it.subtitle)
            }
            if (it.summary) {
                builder.summary(it.summary)
            }
            if (it.content) {
                builder.contentEncoded(MarkdownUtil.htmlFromMarkdown(it.content))
            }

            RssItemEnclosure.Builder enclosureBuilder = RssItemEnclosure.builder()
            Enclosure enclosure = it.enclosure
            if (enclosure.url) {
                enclosureBuilder.url(enclosure.url)
            }
            if (enclosure.type) {
                enclosureBuilder.type(enclosure.type)
            }
            if (enclosure.length) {
                enclosureBuilder.length(enclosure.length)
            }
            builder.enclosure(enclosureBuilder.build())
            podcastBuilder.item(builder.build())
        }

        ItunesPodcast itunesPodcast = podcastBuilder.build()

        File outputFile = new File(output.get().absolutePath + File.separator + RSS_FILE)
        FileWriter writer = new FileWriter(outputFile)
        RssFeedRenderer rssFeedRenderer = new DefaultItunesPodcastRenderer()
        rssFeedRenderer.render(writer, itunesPodcast)
        writer.close()
    }

    List<MarkdownEpisode> parseEpisodes(File folder) {
        List<MarkdownEpisode> listOfEpisodes = []
        folder.eachFile { file ->
            if (file.path.endsWith(SUFFIX_MD) || file.path.endsWith(SUFFIX_MARKDOWN)) {
                ContentAndMetadata cm = parseFile(file)
                listOfEpisodes << new MarkdownEpisode(filename: file.name, content: cm.content, metadata: cm.metadata)
            }
        }
        listOfEpisodes
    }

    static ContentAndMetadata parseFile(File file) {
        String line = null
        List<String> lines = []
        Map<String, String> metadata = [:]
        boolean metadataProcessed = false
        file.withReader { reader ->
            while ((line = reader.readLine()) != null) {
                if (line.contains(SEPARATOR)) {
                    metadataProcessed = true
                    continue
                }
                if (!metadataProcessed && line.contains(COLON)) {
                    String metadataKey = line.substring(0, line.indexOf(COLON as String)).trim()
                    String metadataValue = line.substring(line.indexOf(COLON as String) + COLON.length()).trim()
                    metadata[metadataKey] = metadataValue
                }
                if (metadataProcessed) {
                    lines << line
                }
            }
        }
        boolean empty = (!metadataProcessed || lines.isEmpty())
        new ContentAndMetadata(metadata: empty ? ([:] as Map<String, String>) : metadata,
                content: empty ? file.text : lines.join("\n"))
    }
}
