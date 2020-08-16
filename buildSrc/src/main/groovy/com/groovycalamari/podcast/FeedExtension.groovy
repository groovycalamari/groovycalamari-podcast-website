package com.groovycalamari.podcast

import groovy.transform.CompileStatic
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

import javax.inject.Inject

@CompileStatic
class FeedExtension {
    final Property<File> episodes
    final Property<File> output
    final Property<String> title
    final Property<String> link
    final Property<String> copyright
    final Property<String> language
    final Property<String> keywords
    final Property<String> description
    final Property<String> subtitle
    final Property<String> author
    final Property<String> type
    final Property<String> ownerName
    final Property<String> ownerEmail
    final Property<String> imageUrl
    final Property<String> imageLink
    final Property<Boolean> block
    final Property<String> categories
    final Property<Boolean> explicit

    @Inject
    FeedExtension(ObjectFactory objects) {
        episodes = objects.property(File)
        output = objects.property(File)
        title = objects.property(String)
        link = objects.property(String)
        copyright = objects.property(String)
        language = objects.property(String)
        keywords = objects.property(String)
        description = objects.property(String)
        subtitle = objects.property(String)
        author = objects.property(String)
        type = objects.property(String)
        ownerName = objects.property(String)
        ownerEmail = objects.property(String)
        imageUrl = objects.property(String)
        imageLink = objects.property(String)
        block = objects.property(Boolean)
        categories = objects.property(String)
        explicit = objects.property(Boolean)
    }
}
