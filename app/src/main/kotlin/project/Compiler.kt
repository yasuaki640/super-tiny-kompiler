package project

import project.module.CodeGenerator
import project.module.Parser
import project.module.Tokenizer
import project.module.Transformer

class Compiler {
    fun exec(input: String): String {
        val tokens = Tokenizer().exec(input)
        val ast = Parser().exec(tokens)
        val newAst = Transformer().exec(ast)
        val output = CodeGenerator().exec(newAst)

        return output
    }
}

fun main(args: Array<String>) {
    val input = args.first()
    val compiler = Compiler()
    val output = compiler.exec(input)

    println(output)
}