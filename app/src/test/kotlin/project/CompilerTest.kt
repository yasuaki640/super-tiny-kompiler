package project

import project.module.*
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

        val call = result.body[0]
        assertIs<CallExpression>(call)
        assertEquals(call.name, "add")

        val number = call.params[0]
        assertIs<NumberLiteral>(number)
        assertEquals(number.value, "2")

        val callInner = call.params[1]
        assertIs<CallExpression>(callInner)
        assertEquals(callInner.name, "subtract")

        val leftInnerNumber = callInner.params[0]
        assertIs<NumberLiteral>(leftInnerNumber)
        assertEquals(leftInnerNumber.value, "4")

        val rightInnerNumber = callInner.params[1]
        assertIs<NumberLiteral>(rightInnerNumber)
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

        val expression = expressionStatement.expression
        val callee = expression.callee
        assertEquals(callee, Identifier("add"))

        val arguments = expression.arguments
        assertEquals(arguments[0], CNumberLiteral("2"))

        val callExpression = arguments[1]
        assertIs<CCallExpression>(callExpression)
        assertEquals(callExpression.callee, Identifier("subtract"))

        val innerArguments = callExpression.arguments
        assertEquals(innerArguments[0], CNumberLiteral("4"))
        assertEquals(innerArguments[1], CNumberLiteral("2"))
    }

    @Test
    fun testCodeGeneratorCanGenerateCLikeCode() {
        val generator = CodeGenerator()
        val result = generator.exec(
            CProgram(
                arrayListOf(
                    CExpressionStatement(
                        CCallExpression(
                            Identifier("add"), arrayListOf(
                                CNumberLiteral("2"), CCallExpression(
                                    Identifier("subtract"), arrayListOf(
                                        CNumberLiteral("4"), CNumberLiteral("2")
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        assertEquals(result, "add(2, subtract(4, 2));")
    }

    @Test
    fun testCompilerCanCompileLispLikeToClike() {
        val compiler = Compiler()
        val result = compiler.exec("(add 2 (subtract 4 2))")

        assertEquals(result, "add(2, subtract(4, 2));")
    }
}
