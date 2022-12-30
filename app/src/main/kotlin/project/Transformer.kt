package project

class Transformer {
    fun transformer(ast: Program): CProgram {
        var newAst = CProgram(ArrayList())

        ast._context = newAst.body

        val visitor = Visitor().enter

        Traverser().exec(ast, visitor)

        return newAst
    }
}