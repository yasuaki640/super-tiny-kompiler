package project

class Transformer {
    fun exec(ast: Program): CProgram {
        var newAst = CProgram(ArrayList())

        ast._context = newAst.body

        val visitor = Visitor().enter
        val traverser = Traverser(visitor)
        traverser.exec(ast)

        return newAst
    }
}