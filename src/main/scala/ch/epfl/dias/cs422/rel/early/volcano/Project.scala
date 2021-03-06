package ch.epfl.dias.cs422.rel.early.volcano

import ch.epfl.dias.cs422.helpers.rel.early.volcano.Operator
import ch.epfl.dias.cs422.helpers.builder.skeleton
import ch.epfl.dias.cs422.helpers.rel.RelOperator.Tuple
import org.apache.calcite.rel.`type`.RelDataType
import org.apache.calcite.rex.RexNode

import scala.jdk.CollectionConverters._

class Project protected (input: Operator, projects: java.util.List[_ <: RexNode], rowType: RelDataType) extends skeleton.Project[Operator](input, projects, rowType) with Operator {
  override def open(): Unit = {
    //println("project open")
    input.open()
  }

  lazy val evaluator: Tuple => Tuple = eval(projects.asScala.toIndexedSeq, input.getRowType)

  override def next(): Tuple = {
    val tuple = input.next()

    if (tuple == null) {
      close()
      //println("project done")
      null
    } else {
      if (evaluator(tuple) == null) {
        next()
      } else {
        //println(evaluator(tuple))
        evaluator(tuple)
      }
    }
  }

  override def close(): Unit = {
    //println("project close")
    input.close()
  }
}