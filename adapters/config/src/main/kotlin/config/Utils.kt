package config

import domain.repo.Repo
import org.reflections.Reflections
import usecases.usecase.UsecaseType

private const val usecasePackage = "usecases.usecase"
private const val repoPackage = "repo"

private val usecases get() = Reflections(usecasePackage).getSubTypesOf(UsecaseType::class.java)
private val repositories get() = Reflections(repoPackage).getSubTypesOf(Repo::class.java)

fun Module.usecasesAndRepos(domain: String) {
    usecases(domain)
    repos(domain)
}

fun Module.usecases(domain: String) {
    TODO("Not yet implemented")
}

fun Module.repos(domain: String) {

}
