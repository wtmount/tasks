package typenatural

object NaturalNumber {
  sealed trait Nat {
    type Add[N <: Nat] <: Nat
    type Multiply[N <: Nat] <: Nat
    type ReversePow[N <: Nat] <: Nat
    type Pow[N <: Nat] = N#ReversePow[this.type]
    type Fact <: Nat
    type Sum <: Nat
    type Dec <: Nat
    type ReverseSubtract[N <: Nat] <: Nat
    type Subtract[N <: Nat] = N#ReverseSubtract[this.type]
    type Fib <: Nat
  }

  trait _0 extends Nat {
    type Add[N <: Nat] = N
    type Multiply[N <: Nat] = _0
    type ReversePow[N <: Nat] = _1
    type Fact = _1
    type Sum = _0
    type Dec = _0
    type ReverseSubtract[N <: Nat] = N
    type Fib = _1
  }

  trait Inc[N <: Nat] extends Nat {
    type Add[N2 <: Nat] = Inc[N#Add[N2]]
    type Multiply[N2 <: Nat] = N2#Add[N#Multiply[N2]]
    type ReversePow[N2 <: Nat] = N2#Multiply[N#ReversePow[N2]]
    type Fact = Inc[N]#Multiply[N#Fact]
    type Sum = Inc[N]#Add[N#Sum]
    type Dec = N
    type ReverseSubtract[N2 <: Nat] = N#ReverseSubtract[N2#Dec]
    type Fib = N#Fib#Add[N#Dec#Fib]
  }

  implicitly[_3#Add[_5] =:= _8]
  implicitly[_3#Multiply[_5] =:= _15]
  implicitly[_2#Pow[_4] =:= _16]
  implicitly[_4#Fact =:= _24]
  implicitly[_5#Sum =:= _15]
  implicitly[_20#Subtract[_8] =:= _12]
  implicitly[_6#Fib =:= _21]

  type _1 = Inc[_0]
  type _2 = Inc[_1]
  type _3 = Inc[_2]
  type _4 = Inc[_3]
  type _5 = Inc[_4]
  type _6 = Inc[_5]
  type _7 = Inc[_6]
  type _8 = Inc[_7]
  type _9 = Inc[_8]
  type _10 = Inc[_9]
  type _11 = Inc[_10]
  type _12 = Inc[_11]
  type _13 = Inc[_12]
  type _14 = Inc[_13]
  type _15 = Inc[_14]
  type _16 = Inc[_15]
  type _17 = Inc[_16]
  type _18 = Inc[_17]
  type _19 = Inc[_18]
  type _20 = Inc[_19]
  type _21 = Inc[_20]
  type _22 = Inc[_21]
  type _23 = Inc[_22]
  type _24 = Inc[_23]
  type _25 = Inc[_24]
}
