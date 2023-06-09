package auth

import at.favre.lib.crypto.bcrypt.BCrypt
import domain.entity.user.Password
import domain.entity.user.PasswordHash
import usecases.dependency.PasswordEncoder

class PasswordEncoderImpl : PasswordEncoder {
    override fun encode(password: Password): PasswordHash =
        PasswordHash(BCrypt.withDefaults().hashToString(12, password.value.toCharArray()))


    override fun matches(password: Password, hash: PasswordHash): Boolean =
        BCrypt.verifyer().verify(password.value.toCharArray(), hash.value).verified

}