package com.vynix.android.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizCategory(
    val id: String,
    val name: String,
    val description: String,
    val iconUrl: String? = null
)

@Serializable
data class QuizSessionStartRequest(
    val categoryId: String,
    val difficulty: String   // "easy", "medium", "hard"
)

@Serializable
data class QuizSessionStartResponse(
    val sessionId: String
)

@Serializable
data class QuizOption(
    val id: String,
    val text: String
)

@Serializable
data class QuizQuestion(
    val id: String,
    val text: String,
    val options: List<QuizOption>,
    val timeLimitSeconds: Int? = null
)

@Serializable
data class QuizAnswerRequest(
    val questionId: String,
    val optionId: String
)

@Serializable
data class QuizAnswerResponse(
    val correct: Boolean,
    val pointsEarned: Int,
    val nextQuestionId: String?,
    val sessionComplete: Boolean
)

@Serializable
data class QuizResult(
    val totalCorrect: Int,
    val totalQuestions: Int,
    val pointsEarned: Int,
    val accuracy: Float
)
