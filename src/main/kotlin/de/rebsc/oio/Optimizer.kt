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
import de.rebsc.oio.data.OSMWay

class Optimizer : OSMIndoorOptimizer {

    override fun mergeOverlaps(data: OSMDataSet): OSMDataSet {
        TODO("Not yet implemented")
    }

    override fun mergeCloseNodes(data: OSMDataSet, mergeDistance: Double): OSMDataSet {
        TODO("Not yet implemented")
    }

    override fun orthogonalizeShape(way: OSMWay): OSMWay {
        TODO("Not yet implemented")
    }

}