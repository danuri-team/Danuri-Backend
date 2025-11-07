package org.aing.danurirest.domain.admin.service.excel

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.aing.danurirest.domain.admin.service.excel.config.ExcelProperties
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.aing.danurirest.persistence.form.entity.Form
import org.aing.danurirest.persistence.user.Age
import org.aing.danurirest.persistence.user.Sex
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * 폼 데이터 파싱을 담당하는 컴포넌트
 */
@Component
class FormDataParser(
    private val objectMapper: ObjectMapper,
    private val properties: ExcelProperties,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(FormDataParser::class.java)
    }

    /**
     * 폼 스키마에서 라벨 목록 추출
     * @param form 폼 엔티티
     * @return 라벨 목록
     * @throws FormSchemaParsingException 파싱 실패 시
     */
    fun extractFormLabels(form: Form): List<String> {
        try {
            val formSchema = objectMapper.readTree(form.formSchema)

            if (formSchema?.isArray != true) {
                logger.warn("폼 스키마가 배열이 아닙니다: formId=${form.id}")
                return emptyList()
            }

            return formSchema.mapNotNull { field ->
                field?.get("label")?.asText()
            }
        } catch (e: Exception) {
            logger.error("폼 스키마 파싱 실패: formId=${form.id}", e)
        }
        return emptyList()
    }

    /**
     * 폼 결과 JSON 파싱
     * @param formResult JSON 문자열
     * @return JsonNode
     * @throws FormResultParsingException 파싱 실패 시
     */
    fun parseFormResult(formResult: String): JsonNode {
        try {
            return objectMapper.readTree(formResult)
        } catch (e: Exception) {
            logger.error("폼 결과 파싱 실패: $formResult", e)
            throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
        }
    }

    /**
     * 폼 결과에서 사용자 연령 추출
     * @param formResultJson 폼 결과 JSON 문자열
     * @return Age 또는 null
     */
    fun extractUserAge(formResultJson: String?): Age? {
        if (formResultJson.isNullOrBlank()) {
            return null
        }

        return try {
            val formResult = objectMapper.readTree(formResultJson)
            val ageOption = formResult?.get(properties.form.ageField)?.asText()

            val age = mapAgeOption(ageOption)

            if (age != null) {
                logger.trace("연령 파싱 성공: {} -> {}", ageOption, age)
            } else {
                logger.trace("연령 매핑 실패: {}", ageOption)
            }

            age
        } catch (e: Exception) {
            logger.warn("연령 파싱 중 오류 발생: $formResultJson", e)
            null
        }
    }

    /**
     * 폼 결과에서 사용자 성별 추출
     * @param formResultJson 폼 결과 JSON 문자열
     * @return Sex 또는 null
     */
    fun extractUserSex(formResultJson: String?): Sex? {
        if (formResultJson.isNullOrBlank()) {
            return null
        }

        return try {
            val formResult = objectMapper.readTree(formResultJson)
            val sexOption = formResult?.get(properties.form.sexField)?.asText()

            val sex = mapSexOption(sexOption)

            if (sex != null) {
                logger.trace("성별 파싱 성공: {} -> {}", sexOption, sex)
            } else {
                logger.trace("성별 매핑 실패: {}", sexOption)
            }

            sex
        } catch (e: Exception) {
            logger.warn("성별 파싱 중 오류 발생: $formResultJson", e)
            null
        }
    }

    /**
     * 연령 옵션을 Age enum으로 매핑
     */
    private fun mapAgeOption(ageOption: String?): Age? =
        when (ageOption) {
            "초등학교" -> Age.ELEMENTARY
            "중학교" -> Age.MIDDLE
            "고등학교" -> Age.HIGH
            "대학교" -> Age.COLLEGE
            "학교 밖 청소년" -> Age.OUT_OF_SCHOOL_YOUTH
            "성인/유아" -> Age.ADULT
            else -> null
        }

    /**
     * 성별 옵션을 Sex enum으로 매핑
     */
    private fun mapSexOption(sexOption: String?): Sex? =
        when (sexOption) {
            "남" -> Sex.MALE
            "여" -> Sex.FEMALE
            else -> null
        }
}
