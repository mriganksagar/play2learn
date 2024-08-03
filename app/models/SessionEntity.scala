package models

import java.util.UUID

final case class SessionEntity(sessionId: UUID, username: String)