package com.example.untitled.apiImpl.entity

import com.example.untitled.api.player.Player
import java.util.*

class PlayerImpl(override val name: String, override val uuid: UUID) :
    Player, SelectableEntityImpl(uuid) {}
