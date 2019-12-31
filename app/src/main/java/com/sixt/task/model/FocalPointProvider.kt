package com.sixt.task.model

import com.sixt.task.model.vo.Point

interface FocalPointProvider {

    fun getFocalPoint(points: List<Point>) : Point
}