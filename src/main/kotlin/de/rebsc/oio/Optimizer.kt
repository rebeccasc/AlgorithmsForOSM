package de.rebsc.oio

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

import de.rebsc.oio.api.OSMIndoorOptimizer
import de.rebsc.oio.data.OSMDataSet
import de.rebsc.oio.data.OSMNode
import de.rebsc.oio.data.OSMWay
import de.rebsc.oio.data.Point2D
import de.rebsc.oio.tools.DBSCAN
import de.rebsc.oio.tools.Merger
import de.rebsc.oio.utils.IdGenerator

class Optimizer : OSMIndoorOptimizer {

    override fun mergeOverlaps(data: OSMDataSet): OSMDataSet {
        return Merger.mergeOverlaps(data)
    }

    override fun mergeCloseNodes(data: OSMDataSet, mergeDistance: Double): OSMDataSet {
        val dataClone = data.copy()

        // cluster points
        val points = ArrayList<Point2D>()
        data.nodes.forEach { node -> points.add(Point2D(node.x, node.y)) }
        val clusteredData = DBSCAN().performClustering(points, mergeDistance, 1)

        // merge points in cluster
        clusteredData.forEach { cluster ->
            if (cluster.isEmpty()) throw OptimizerException("Found empty cluster")
            if (cluster.size < 2) return@forEach

            // get centroid
            val allX = ArrayList<Double>()
            val allY = ArrayList<Double>()
            cluster.forEach { point ->
                allX.add(point.x)
                allY.add(point.y)
            }
            val nodeToGetTagsFrom = (data.nodes.filter { it.x == cluster[0].x && it.y == cluster[0].y })[0]
            val centroidNode = OSMNode(
                IdGenerator.createUUID(true),
                Point2D(allX.average(), allY.average()),
                nodeToGetTagsFrom.tags
            )

            // replace nodes
            data.nodes.forEach { point ->
                cluster.forEach { clusterPoint ->
                    if (point.x == clusterPoint.x && point.y == clusterPoint.y) {
                        dataClone.nodes.remove(point)
                    }
                }
            }
            dataClone.nodes.add(centroidNode)

            // replace nodes in ways
            for (i in data.ways.indices) {
                val way = data.ways[i]
                for (j in way.points.indices) {
                    val point = way.points[j]
                    val inCluster = cluster.filter { cPoint -> cPoint.x == point.x && cPoint.y == point.y }
                    if (inCluster.isNotEmpty()) {
                        val includesCentroid =
                            way.points.filter { wPoint -> wPoint.x == centroidNode.x && wPoint.y == centroidNode.y }
                        if (includesCentroid.isNotEmpty()) {
                            // remove
                            dataClone.ways[i].points.remove(dataClone.ways[i].points[j])
                        } else {
                            // replace
                            dataClone.ways[i].points[j] = centroidNode
                        }
                    }
                }
            }

        }

        return dataClone
    }

    override fun orthogonalizeShape(way: OSMWay): OSMWay {
        TODO("Not yet implemented")
    }

    override fun clusterPointsByDBSCAN(
        points: ArrayList<Point2D>,
        maxDistance: Double,
        minPoints: Int
    ): ArrayList<ArrayList<Point2D>> {
        return DBSCAN().performClustering(points, maxDistance, minPoints)
    }

    class OptimizerException(msg: String) : Exception(msg)
}