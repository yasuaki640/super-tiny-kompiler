package project

class Compiler {
    fun exec(input: String): String {
        val tokens = Tokenizer().exec(input)
        val ast = Parser().exec(tokens)
        val newAst = Transformer().exec(ast)
        val output = CodeGenerator().exec(newAst)

        return output
    }
}