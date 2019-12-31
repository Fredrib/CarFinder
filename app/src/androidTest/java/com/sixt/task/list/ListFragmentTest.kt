package com.sixt.task.list

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sixt.task.view.list.ListActivity
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListFragmentTest {

    @get:Rule
    val activityRule = IntentsTestRule(ListActivity::class.java, false, false)

    private lateinit var robot: ListRobot
    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        robot = ListRobot(activityRule)
        mockWebServer.start(8080)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun someTest() {

    }
}