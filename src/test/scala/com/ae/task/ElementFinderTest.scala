package com.ae.task

import org.scalatest.FlatSpec
import org.scalatest.Matchers._

import scala.io.Source

class ElementFinderTest extends FlatSpec {

  private val resourcePaths = List(
    "sample-0-origin.html",
    "sample-1-evil-gemini.html",
    "sample-2-container-and-clone.html",
    "sample-3-the-escape.html",
    "sample-4-the-mash.html"
  )

  private val id = "make-everything-ok-button"
  private val htmlOriginal = Source.fromResource(resourcePaths.head).mkString

    // TODO add more test, check for invalid cases

  "The element finder" should "find the Everything Ok button in a similar html in sample 1" in {

    val selector = "#page-wrapper > div.row:nth-child(3) > div.col-lg-8 > div.panel.panel-default > div.panel-body > a.btn.btn-success"

    validate(1, id, selector)
  }

  "The element finder" should "find the Everything Ok button in a similar html in sample 2" in {

    val selector = "#page-wrapper > div.row:nth-child(3) > div.col-lg-8 > div.panel.panel-default > div.panel-body > div.some-container > a.btn.test-link-ok"
    validate(2, id, selector)

  }

  "The element finder" should "find the Everything Ok button in a similar html in sample 3" in {

    val selector = "#page-wrapper > div.row:nth-child(3) > div.col-lg-8 > div.panel.panel-default > div.panel-footer > a.btn.btn-success"
    validate(3, id, selector)
  }

  "The element finder" should "find the Everything Ok button in a similar html in sample 4" in {

    val selector = "#page-wrapper > div.row:nth-child(3) > div.col-lg-8 > div.panel.panel-default > div.panel-footer > a.btn.btn-success"
    validate(4, id, selector)
  }

  private def validate(sampleNumber: Int, id: String, selector: String) = {
    val modified = resourcePaths(sampleNumber)

    val htmlModified = Source.fromResource(modified).mkString

    val finder = new ElementFinder(htmlOriginal, htmlModified)
    val result = finder.findById(id)

    assert(result.nonEmpty)

    result.get should be(selector)
  }
}
