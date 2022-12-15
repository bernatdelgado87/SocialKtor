package com.example.domain.usecase.front.challenge

import com.example.data.application.executor.JobCoroutine
import com.example.data.repository.FileUploader
import com.example.data.repository.front.ChallengeRepositoryImpl
import com.example.domain.model.course.CHALLENGE_FILES_URL_RELATIVE
import com.example.domain.model.course.ChallengeModel
import com.example.domain.repository.front.ChallengeRepository
import es.experis.app.domain.executor.BackgroundCoroutine
import es.experis.app.domain.usecase.UseCase
import io.ktor.http.content.*
import kotlinx.coroutines.flow.Flow

class UpdateChallengeUseCase(
    private val repository: ChallengeRepository = ChallengeRepositoryImpl(),
    backgroundCoroutine: BackgroundCoroutine = JobCoroutine()
) : UseCase<ChallengeModel, UpdateChallengeUseCase.Input>(backgroundCoroutine) {
    data class Input(val challengeModel: ChallengeModel, val fileItems: List<PartData.FileItem>?)

    override suspend fun run(params: Input): Flow<ChallengeModel> {
        if (params.fileItems != null && !params.fileItems.isEmpty()) {
            for (file in params.fileItems) {
                val inputStream = file.streamProvider()
                FileUploader.uploadFileToAWS(CHALLENGE_FILES_URL_RELATIVE + file.originalFileName, inputStream = inputStream)
            }
        }
        return repository.updateChallenge(params.challengeModel)
    }
}
