package com.groovycalamari.podcast

import groovy.transform.CompileStatic
import groovy.transform.ToString
import io.micronaut.rss.itunespodcast.ItunesPodcastEpisodeType

@ToString
@CompileStatic
class MarkdownEpisode {
    private static final String KEY_TITLE = 'title'
    private static final String KEY_AUTHOR = 'author'
    private static final String KEY_EPISODE_TYPE = 'episodeType'
    private static final String KEY_SUBTITLE = 'subtitle'
    private static final String KEY_SUMMARY = 'summary'
    private static final String KEY_DESCRIPTION = 'description'
    private static final String KEY_ENCLOSURE_URL = 'enclosureUrl'
    private static final String KEY_ENCLOSURE_TYPE = 'enclosureType'
    private static final String KEY_ENCLOSURE_LENGTH = 'enclosureLength'
    private static final String KEY_GUID = 'guid'
    private static final String KEY_DURATION = 'duration'
    private static final String KEY_IMAGE = 'image'
    private static final String KEY_EPISODE = 'episode'
    private static final String KEY_SEASON = 'season'
    private static final String KEY_EXPLICIT = 'explicit'

    String filename
    Map<String, String> metadata
    String content

    Integer getEpisode() {
        metadata[KEY_EPISODE] ? Integer.valueOf(metadata[KEY_EPISODE]) : null
    }

    Integer getSeason() {
        metadata[KEY_SEASON] ? Integer.valueOf(metadata[KEY_SEASON]) : null
    }

    Boolean getExplicit() {
        metadata[KEY_EXPLICIT] ? Boolean.valueOf(metadata[KEY_EXPLICIT]) : null
    }

    String getDuration() {
        metadata[KEY_DURATION]
    }

    String getImage() {
        metadata[KEY_IMAGE]
    }

    String getGuid() {
        metadata[KEY_GUID]
    }

    String getTitle() {
        metadata[KEY_TITLE]
    }

    String getAuthor() {
        metadata[KEY_AUTHOR]
    }

    String getSubtitle() {
        metadata[KEY_SUBTITLE]
    }

    String getSummary() {
        metadata[KEY_SUMMARY]
    }

    String getDescription() {
        metadata[KEY_DESCRIPTION]
    }

    ItunesPodcastEpisodeType getEpisodeType() {
        metadata[KEY_EPISODE_TYPE] ?  metadata[KEY_EPISODE_TYPE] as ItunesPodcastEpisodeType : null
    }

    Enclosure getEnclosure() {
        new Enclosure(url: metadata[KEY_ENCLOSURE_URL],
                type: metadata[KEY_ENCLOSURE_TYPE],
                length: metadata[KEY_ENCLOSURE_LENGTH] ? Integer.valueOf(metadata[KEY_ENCLOSURE_LENGTH]) : null)
    }
}

