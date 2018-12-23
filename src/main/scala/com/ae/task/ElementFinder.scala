package com.ae.task

import org.jsoup.Jsoup
import org.jsoup.nodes.{Attribute, Element}
import org.jsoup.parser.Tag
import com.typesafe.scalalogging.LazyLogging


import scala.collection.JavaConverters._

/**
  * Finder of the element from the original html in the modified html.
  *
  * @param original the original html as String
  * @param modified the modified html as String
  */
class ElementFinder(original: String, modified: String) extends LazyLogging {

  type SelectorPath = String
  type Elements = List[Element]

  private val jsoupOriginal = Jsoup.parse(original)
  private val jsoupModified = Jsoup.parse(modified)

  def findById(id: String): Option[SelectorPath] = {
    val originalElement = Option(jsoupOriginal.getElementById(id)).getOrElse({
      // TODO escalate error to main and handle there
      logger.error(s"Invalid application parameter [id: $id]")
      sys.exit(1)
    }
    )

    val attrs = originalElement
      .attributes()
      .asScala
      .toList

    // take name of the classes since they are the most reliable attribute
    val clss: List[String] = originalElement.classNames().asScala.toList

    // the tag's name probably would remain the same
    val tag = originalElement.tag()

    findSimilar(attrs, clss, tag)
  }

  private def findSimilar(attributes: List[Attribute],
                          clss: List[String],
                          tag: Tag): Option[SelectorPath] = {
    // TODO refactor me
    var candidates: List[Elements] = for (cls <- clss)
      yield
        jsoupModified
          .getElementsByAttributeValueContaining("class", cls) // the class name may not be the same but probably is similar
          .asScala
          .toList
          .filter(element =>
            areSimilar(element.attributes().asScala.toList, attributes))

    candidates = candidates.filter(_.nonEmpty)

    val elements =
      for (elements <- candidates if elements.exists(_.tag() == tag))
        yield elements.find(_.tag() == tag)
    if (elements.isEmpty) None
    else {
      elements.head.map(_.cssSelector())

      val targetElement = candidates.head.find(_.tag() == tag)
      targetElement.map(_.cssSelector())
    }
  }

  private def areSimilar(attributes: List[Attribute],
                         attributes1: List[Attribute]): Boolean = {
    //TODO improve logic
    // check that the number of attributes don't differ by more than one
    Math.abs(attributes.length - attributes1.length) <= 1
  }
}

// List()
