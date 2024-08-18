# Algorithms for OSM
[![CI](https://github.com/rebeccasc/OSMIndoorOptimizer/actions/workflows/gradle.yml/badge.svg?branch=master)](https://github.com/rebeccasc/OSMIndoorOptimizer/actions/workflows/gradle.yml)
[![license: AGPLv3](https://img.shields.io/badge/license-AGPLv3-blue.svg?style=flat-square&maxAge=7200)](https://github.com/rebeccasc/OSMIndoorOptimizer/blob/master/LICENSE)

Collection of algorithms to handle (filter, sort, merge, ...) OSM data

## Algorithms
* Merge overlapping areas
* Merge close nodes to reduce data set complexity 
* (Orthogonalize shape of ways) - **Not yet implemented**
* Clustering nodes by DBSCAN clustering algorithm

_Looking for another tool? Add one by creating a pull request or share your idea in a new issue!_

## How to use

### API
```kotlin
/**
 * Merge overlapping areas
 * @param data set to optimize
 * @return optimized data set
 */
fun mergeOverlaps(data: OSMDataSet): OSMDataSet

/**
 * Merge close nodes
 * @param data set to optimize
 * @param mergeDistance minimum distance between two nodes to merge
 * @return optimized data set
 */
fun mergeCloseNodes(data: OSMDataSet, mergeDistance: Double): OSMDataSet

/**
 * Orthogonalize shape of way
 * @param way to optimize
 * @return optimized way
 */
// fun orthogonalizeShape(way: OSMWay): OSMWay  // Not yet implemented

/**
 * Cluster set of points with using DBSCAN clustering algorithm
 * @param points to cluster
 * @param maxDistance of cluster in meter. Needs to be greater than 0.0
 * @param minPoints kept in one cluster. Needs to be greater than 0
 * @return [ArrayList] holding determined clusters as [ArrayList]s including points
 */
fun clusterPointsByDBSCAN(
    points: ArrayList<Point2D>,
    maxDistance: Double,
    minPoints: Int
): ArrayList<ArrayList<Point2D>>
    
```
### Example

```kotlin
// merge overlapping areas in data set
val myData = OSMDataSet()
val myDataOptimized = Optimizer().mergeOverlaps(myData)
```

## Dependencies
* [JTS Topology Suite](https://github.com/locationtech/jts)


