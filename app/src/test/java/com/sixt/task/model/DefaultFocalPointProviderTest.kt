package com.sixt.task.model

import com.sixt.task.model.vo.Point
import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultFocalPointProviderTest {

    private val focalPointProvider = DefaultFocalPointProvider()

    @Test
    fun `Given a list of vertices of a convex polygon, when the getFocalPoint method is called with the vertex list, the polygon center must be returned`(){
        val vertices = listOf(
            Point(0.0, 0.0),
            Point(0.0, 10.0),
            Point(10.0, 10.0),
            Point(10.0, 0.0)
        )

        val expectedCenter = Point(5.0, 5.0)

        val center = focalPointProvider.getFocalPoint(vertices)

        assertEquals(expectedCenter, center)
    }


}