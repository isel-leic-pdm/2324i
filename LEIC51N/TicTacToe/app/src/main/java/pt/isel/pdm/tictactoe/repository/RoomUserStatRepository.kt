package pt.isel.pdm.tictactoe.repository

import pt.isel.pdm.tictactoe.database.UserStatDao
import pt.isel.pdm.tictactoe.database.model.UserStat

class RoomUserStatRepository(
    private val dao: UserStatDao
) : UserStatRepository {
    override suspend fun getAllStats(): List<UserStat> {
        return dao.getAll()
    }

    override suspend fun getStat(userName: String): UserStat {
        val ret = dao.findByName(userName)

        if(ret == null)
            throw Exception("Could not found $userName")

        return ret
    }

    override suspend fun updateStat(userStat: UserStat) {
        dao.update(userStat)
    }

    override suspend fun createStat(userName: String) : UserStat{
        val obj = UserStat(userName, 0, 0, 0)
        dao.insert(obj)
        return obj
    }
}