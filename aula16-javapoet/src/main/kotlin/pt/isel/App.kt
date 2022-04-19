package pt.isel

import com.squareup.javapoet.JavaFile
import java.io.File
import java.net.URLClassLoader
import javax.tools.ToolProvider
import kotlin.reflect.full.declaredFunctions


fun main() {
    val myDynamic = loadAndCreateInstance(buildMyDynamic(), 29) as Multiplier
    /*
    val res = myDynamic::class
        .declaredFunctions
        .find { it.name == "mul" }
        ?.call(myDynamic, 2)
     */
    println(myDynamic.mul(2))
}


private val root = File("./build")
private val classLoader = URLClassLoader.newInstance(arrayOf(root.toURI().toURL()))
private val compiler = ToolProvider.getSystemJavaCompiler()

fun loadAndCreateInstance(source: JavaFile, vararg args: Any): Any {
    // Save source in .java file.
    source.writeToFile(root)

    // Compile source file.
    compiler.run(null, null, null, "${root.path}/${source.typeSpec.name}.java")

    // Load and instantiate compiled class.
    return classLoader
        .loadClass(source.typeSpec.name)
        .declaredConstructors[0]
        .newInstance(*args)
}