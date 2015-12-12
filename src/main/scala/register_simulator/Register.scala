package register_simulator

class Register(val index: Int) {
  override def toString = s"r$index"

  def canEqual(other: Any): Boolean = other.isInstanceOf[Register]

  override def equals(other: Any): Boolean = other match {
    case that: Register =>
      (that canEqual this) &&
        index == that.index
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(index)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}