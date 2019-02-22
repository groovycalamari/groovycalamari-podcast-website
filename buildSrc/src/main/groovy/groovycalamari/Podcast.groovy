package groovycalamari

import groovy.transform.CompileStatic
import groovy.transform.ToString

@ToString(excludes = ['description'])
@CompileStatic
class Podcast {
    String title
    String description


    List<Episode> episodes = []
}
