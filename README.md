# OSMIndoorOptimizer
[![CI](https://github.com/rebeccasc/OSMIndoorOptimizer/actions/workflows/gradle.yml/badge.svg?branch=master)](https://github.com/rebeccasc/OSMIndoorOptimizer/actions/workflows/gradle.yml)
[![license: AGPLv3](https://img.shields.io/badge/license-AGPLv3-blue.svg?style=flat-square&maxAge=7200)](https://github.com/rebeccasc/OSMIndoorOptimizer/blob/master/LICENSE)

Library of tools to optimize OSM indoor data 

## Tools
* Merge overlapping areas
* Merge close nodes to reduce data set complexity 
* Orthogonalize shape of ways

_Looking for another tool? Add one by creating a pull request or share your idea in a new issue!_

## How to use

:construction: **In the development stage** :construction:

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
fun orthogonalizeShape(way: OSMWay): OSMWay
```
### Example

```kotlin
// merge overlapping areas in data set
val myData = OSMDataSet()
val myDataOptimized = Optimizer().mergeOverlaps(myData)
```

## Dependencies
* [JTS Topology Suite](https://github.com/locationtech/jts)

## Authors
Rebecca Schmidt (rebeccasmdt@gmail.com)

