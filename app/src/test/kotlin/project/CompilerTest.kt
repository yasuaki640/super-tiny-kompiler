/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package project

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CompilerTest {
    @Test
    fun testTokenizerCanAnalyzeInput() {
        val result = Tokenizer().exec("(add 2 (subtract 4 2))")
        assertEquals(
            result, arrayListOf(
                Token("paren", "("),
                Token("name", "add"),
                Token("number", "2"),
                Token("paren", "("),
                Token("name", "subtract"),
                Token("number", "4"),
                Token("number", "2"),
                Token("paren", ")"),
                Token("paren", ")")
            )
        )
    }

    @Test
    fun testParserCanParseAst() {
        val result = Parser().exec(
            arrayListOf(
                Token("paren", "("),
                Token("name", "add"),
                Token("number", "2"),
                Token("paren", "("),
                Token("name", "subtract"),
                Token("number", "4"),
                Token("number", "2"),
                Token("paren", ")"),
                Token("paren", ")")
            )
        )

        assertIs<Program>(result)

        val call = result.body[0] as CallExpression
        assertIs<CallExpression>(call)
        assertEquals(call.name, "add")

        val number = call.params[0] as NumberLiteral
        assertIs<NumberLiteral>(number)
        assertEquals(number.value, "2")

        val callInner = call.params[1] as CallExpression
        assertIs<CallExpression>(callInner)
        assertEquals(callInner.name, "subtract")

        val leftInnerNumber = callInner.params[0] as NumberLiteral
        assertIs<NumberLiteral>(leftInnerNumber)
        assertEquals(leftInnerNumber.value, "4")

        val rightInnerNumber = callInner.params[1] as NumberLiteral
        assertEquals(rightInnerNumber.value, "2")
    }

    @Test
    fun transformerCanTransformAstToNewAst() {
        val transformer = Transformer()

        val result = transformer.exec(
            Program(
                arrayListOf(
                    CallExpression(
                        "add", arrayListOf(
                            NumberLiteral("2"), CallExpression(
                                "subtract", arrayListOf(
                                    NumberLiteral("4"), NumberLiteral("2")
                                )
                            )
                        )
                    )
                )
            )
        )

        val body = result.body
        val expressionStatement = body.first()
        assertIs<CExpressionStatement>(expressionStatement)
    }
}
