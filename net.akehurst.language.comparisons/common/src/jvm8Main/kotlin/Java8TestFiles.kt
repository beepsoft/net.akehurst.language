/**
 * Copyright (C) 2020 Dr. David H. Akehurst (http://dr.david.h.akehurst.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.akehurst.language.comparisons.common

import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.*

data class FileData(
        val index: Int,
        val path: Path,
        val size: Long
)

object Java8TestFiles {
    var javaTestFiles = "../javaTestFiles/javac"

    val files: Collection<FileData>
        get() {
            val params = mutableListOf<Pair<Path, Long>>()
            try {
                val matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.java")
                Files.walkFileTree(Paths.get(javaTestFiles), object : SimpleFileVisitor<Path>() {
                    @Throws(IOException::class)
                    override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                        if (attrs.isRegularFile && matcher.matches(file)) {
                            val size = attrs.size()
                            params.add(Pair(file, size))
                        }
                        return FileVisitResult.CONTINUE
                    }
                })
            } catch (e: IOException) {
                throw RuntimeException("Error getting files", e)
            }
            params.sortBy { it.second }
            var index = 0
            return params.map { FileData(index++, it.first, it.second) }
        }
}