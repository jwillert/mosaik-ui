package dev.jwillert.ktor.vrt

import io.kotest.assertions.fail
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData

abstract class VisualRegressionSpec(scenarios: List<Scenario>) : FunSpec({
    val harness = VrtHarness()
    beforeSpec { harness.start() }
    afterSpec { harness.close() }
    withData(nameFn = { it.name }, scenarios) { scenario ->
        harness.check(scenario)?.let { fail(it) }
    }
})
