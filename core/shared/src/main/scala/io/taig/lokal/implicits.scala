package io.taig.lokal

import io.taig.lokal.instances.AllInstances
import io.taig.lokal.syntax.AllSyntax

trait implicits extends AllInstances with AllSyntax

object implicits extends implicits
