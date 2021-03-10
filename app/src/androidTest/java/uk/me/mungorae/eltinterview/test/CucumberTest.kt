package uk.me.mungorae.eltinterview.test

import io.cucumber.junit.CucumberOptions

/**
 * Class only required for annotations.
 */
@CucumberOptions(
    features = ["features"],
    strict = true,
)
class CucumberTest