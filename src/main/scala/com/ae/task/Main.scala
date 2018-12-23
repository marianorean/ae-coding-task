package com.ae.task

import com.typesafe.scalalogging.LazyLogging

import scala.io.Source
import scala.util.Try

object Main  extends App with LazyLogging {
  if (args.length <= 1) {
    logger.error(
      "Wrong number of arguments.\n\nUsage: sbt run \"Main [input_origin_file_path] [input_other_sample_file_path]\"")
    sys.exit(1)
  }

  val originalHtml = parseHtml(args(0))
  val modifiedHtml = parseHtml(args(1))

  logger.info("Files parsed correctly")

  val defaultId = "make-everything-ok-button"
  val id = Option(sys.props("id")).getOrElse(defaultId)

  logger.info(s"Searching for element with id: $id in original file")


  val finder = new ElementFinder(originalHtml, modifiedHtml)
  println(finder.findById(id))

  def parseHtml(filename: String): String = {
    Try(Source.fromFile(filename).mkString).getOrElse({
      logger.error("Can't parse html file: " + filename)
      sys.exit(1)
    })
  }
}
