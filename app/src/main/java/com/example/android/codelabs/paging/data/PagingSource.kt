package com.example.android.codelabs.paging.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android.codelabs.paging.api.GithubService
import com.example.android.codelabs.paging.api.IN_QUALIFIER
import com.example.android.codelabs.paging.data.GithubRepository.Companion.NETWORK_PAGE_SIZE
import com.example.android.codelabs.paging.model.Repo
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by: androdev
 * Date: 4/2/2022
 * Time: 11:29 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

private const val GITHUB_STARTING_PAGE_INDEX = 1

class PagingSource(
    private val service: GithubService,
    private val query: String
) : PagingSource<Int, Repo>() {
    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX
        val apiQuery = query + IN_QUALIFIER

        return try {
            val response = service.searchRepos(
                query = apiQuery,
                page = position,
                itemsPerPage = params.loadSize
            )
            val repos = response.items
            val nextKey = if(repos.isEmpty()) {
                null
            } else {
                position + params.loadSize / NETWORK_PAGE_SIZE
            }

            LoadResult.Page(
                data = repos,
                prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}