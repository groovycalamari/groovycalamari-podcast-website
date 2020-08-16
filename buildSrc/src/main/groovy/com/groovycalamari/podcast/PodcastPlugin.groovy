package com.groovycalamari.podcast

import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.BasePlugin

@CompileStatic
class PodcastPlugin implements Plugin<Project> {

    public static final String TASK_FEED = "renderPodcastFeed"
    public static final String EXTENSION_FEED = "podcast"
    public static final String TASK_BUILD = "build"

    @Override
    void apply(Project project) {
        project.getPlugins().apply(BasePlugin.class)
        project.extensions.create(EXTENSION_FEED, FeedExtension)

        project.tasks.register(TASK_FEED, FeedTask, { task ->
            Object extension = project.getExtensions().findByName(EXTENSION_FEED)
            if (extension instanceof FeedExtension) {
                FeedExtension siteExtension = ((FeedExtension) extension)
                task.setProperty("output", siteExtension.output)
                task.setProperty("episodes", siteExtension.episodes)
                task.setProperty("title", siteExtension.title)
                task.setProperty("link", siteExtension.link)
                task.setProperty("copyright", siteExtension.copyright)
                task.setProperty("language", siteExtension.language)
                task.setProperty("keywords", siteExtension.keywords)
                task.setProperty("podcastDescription", siteExtension.description)
                task.setProperty("subtitle", siteExtension.subtitle)
                task.setProperty("author", siteExtension.author)
                task.setProperty("type", siteExtension.type)
                task.setProperty("ownerName", siteExtension.ownerName)
                task.setProperty("ownerEmail", siteExtension.ownerEmail)
                task.setProperty("imageUrl", siteExtension.imageUrl)
                task.setProperty("imageLink", siteExtension.imageLink)
                task.setProperty("block", siteExtension.block)
                task.setProperty("categories", siteExtension.categories)
                task.setProperty("explicit", siteExtension.explicit)
            }

            task.setGroup("podcast")
            task.setDescription('Generates Podcast feed')
        })
        project.tasks.named(TASK_BUILD).configure(new Action<Task>() {
            @Override
            void execute(Task task) {
                task.dependsOn(TASK_FEED)
            }
        })
    }
}
