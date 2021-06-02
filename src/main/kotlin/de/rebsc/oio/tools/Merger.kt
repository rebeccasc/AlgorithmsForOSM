package de.rebsc.oio.tools

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

import de.rebsc.oio.data.OSMDataSet
import de.rebsc.oio.data.OSMNode
import de.rebsc.oio.data.OSMWay
import de.rebsc.oio.data.Point2D
import de.rebsc.oio.utils.IdGenerator
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory


class Merger {

    companion object {

        /**
         * Merge overlapping areas
         * @param data set to optimize
         * @return optimized data set
         */
        fun mergeOverlaps(data: OSMDataSet): OSMDataSet {

            for (i in 0 until data.ways.size) {
                for (j in i + 1 until data.ways.size) {
                    val way1 = data.ways[i]
                    val way2 = data.ways[j]

                    val coords1 = arrayOfNulls<Coordinate>(way1.points.size)
                    for (k in 0 until way1.points.size) {
                        coords1[k] = Coordinate(way1.points[k].x, way1.points[k].y)
                    }
                    val coords2 = arrayOfNulls<Coordinate>(way2.points.size)
                    for (k in 0 until way2.points.size) {
                        coords2[k] = Coordinate(way2.points[k].x, way2.points[k].y)
                    }

                    val poly1 = GeometryFactory().createPolygon(coords1)
                    val poly2 = GeometryFactory().createPolygon(coords2)
                    val union: Geometry = poly1.union(poly2)

                    val mergedWay = OSMWay(IdGenerator.createUUID(true))
                    union.coordinates.forEach { c ->
                        mergedWay.points.add(OSMNode(IdGenerator.createUUID(true), Point2D(c.x, c.y)))
                    }
                    mergedWay.points.removeLast()
                    mergedWay.points.add(mergedWay.points[0])

                    data.nodes.removeAll(data.ways[j].points)
                    data.ways.removeAt(j)
                    data.nodes.removeAll(data.ways[i].points)
                    data.ways.removeAt(i)

                    val nodesWithoutLast = deepCopy(mergedWay.points)
                    nodesWithoutLast.removeLast()
                    data.nodes.addAll(nodesWithoutLast)
                    data.ways.add(mergedWay)
                }
            }

            return data
        }

        /**
         * Merge close nodes
         * @param data set to optimize
         * @param mergeDistance minimum distance between two nodes to merge
         * @return optimized data set
         */
        fun mergeCloseNodes(data: OSMDataSet, mergeDistance: Double): OSMDataSet {
            TODO("Not yet implemented")
        }


        // helper

        /**
         * Creates deep copy of [OSMNode] list
         * @param list to create copy of
         * @return deep copy of list
         */
        private fun deepCopy(list: ArrayList<OSMNode>): ArrayList<OSMNode> {
            val deepCopyList = ArrayList<OSMNode>()
            val copy = list.map { it.copy() }
            copy.forEach { node ->
                deepCopyList.add(node)
            }
            return deepCopyList
        }

    }

}