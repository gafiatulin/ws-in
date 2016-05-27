package actor

/**
  * Created by victor on 27/05/16.
  */
sealed trait ActorMessage

case object Inc extends ActorMessage
case object Show extends ActorMessage
case object Shutdown extends ActorMessage
