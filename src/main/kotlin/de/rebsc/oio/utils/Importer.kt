package de.rebsc.oio.utils

import de.rebsc.oio.data.OSMDataSet
import de.rebsc.oio.data.OSMNode
import de.rebsc.oio.data.OSMWay
import de.rebsc.oio.data.Point2D
import java.io.File

/******************************************************************************
 * Copyright (C) 2021  de.rebsc
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see {@literal<http://www.gnu.org/licenses/>}.
 *****************************************************************************/

/**
 * A very basic osm file importer. Supports nodes and ways only
 */
class Importer {

    companion object {

        /**
         * Imports OSM data. Data will be kept in [OSMDataSet]
         * @param filepath to file to import
         * @return [OSMDataSet]
         */
        fun importOSM(filepath: String): OSMDataSet {
            val data = OSMDataSet()
            val nodes = ArrayList<OSMNode>()
            val ways = ArrayList<OSMWay>()
            var mode = "header"
            var counter = 0
            var currentWay = OSMWay(0.toLong())

            File(filepath).forEachLine { line ->
                if (counter > 20 && mode == "header") throw BrokenFileException("Cannot read file")
                if (line.contains("<osm")) mode = "body"

                // nodes
                if (line.contains("<node")) {
                    var id: Long = 0
                    var x = 0.0
                    var y = 0.0
                    val nodeAttributes: List<String> = line.trim().split("\\s+".toRegex())
                    nodeAttributes.forEach { attribute ->
                        if (attribute.contains("id")) id = getValue(attribute).toLong()
                        if (attribute.contains("lat")) y = getValue(attribute).toDouble()
                        if (attribute.contains("lon")) x = getValue(attribute).toDouble()
                    }
                    nodes.add(OSMNode(id, Point2D(x, y)))
                }
                // TODO handle nodes with tags

                // ways
                if (line.contains("<way")) {
                    mode = "way_entered"
                    var id: Long = 0
                    val wayAttributes: List<String> = line.trim().split("\\s+".toRegex())
                    wayAttributes.forEach { attribute ->
                        if (attribute.contains("id")) id = getValue(attribute).toLong()
                    }
                    currentWay = OSMWay(id)
                }
                if (mode == "way_entered" && line.contains("<nd")) {
                    val wayRefs: List<String> = line.trim().split("\\s+".toRegex())
                    val id = getValue(wayRefs[1]).toLong()
                    nodes.forEach { node ->
                        if (node.id == id) currentWay.points.add(node)
                    }
                }
                if (mode == "way_entered" && line.contains("<tag")) {
                    // TODO add tag to way
                }
                if (line.contains("</way>")) {
                    mode = "body"
                    ways.add(currentWay)
                    currentWay = OSMWay(0.toLong())
                }

                ++counter
            }

            data.addNodes(nodes)
            data.addWays(ways)
            return data
        }

        // helper

        /**
         * Extracts attribute value
         * @param attribute to get value from
         * @return value as [String]
         */
        fun getValue(attribute: String): String {
            val attr = attribute.substring(attribute.indexOf("'") + 1)
            return attr.substring(0, attr.indexOf("'"))
        }

    }

    class BrokenFileException(msg: String) : Exception(msg)
}