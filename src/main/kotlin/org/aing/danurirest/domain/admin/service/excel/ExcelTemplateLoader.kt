package org.aing.danurirest.domain.admin.service.excel

import org.aing.danurirest.domain.admin.service.excel.config.ExcelProperties
import org.aing.danurirest.global.exception.CustomException
import org.aing.danurirest.global.exception.enums.CustomErrorCode
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

/**
 * Excel 템플릿 로딩을 담당하는 컴포넌트
 */
@Component
class ExcelTemplateLoader(
    private val properties: ExcelProperties,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(ExcelTemplateLoader::class.java)
    }

    /**
     * 월별 이용 현황 템플릿 로드
     * @return XSSFWorkbook
     * @throws ExcelTemplateLoadException 템플릿 로드 실패 시
     */
    fun loadMonthlyUsageTemplate(): XSSFWorkbook {
        val templatePath = properties.template.path

        try {
            logger.info("Excel 템플릿 로드 시작: $templatePath")

            val templateResource = ClassPathResource(templatePath)

            if (!templateResource.exists()) {
                logger.error("템플릿 파일이 존재하지 않습니다: $templatePath")
                throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
            }

            val workbook = XSSFWorkbook(templateResource.inputStream)

            // 템플릿 검증
            if (workbook.numberOfSheets == 0) {
                logger.error("템플릿에 시트가 없습니다: $templatePath")
                throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
            }

            logger.info("Excel 템플릿 로드 완료: $templatePath, 시트 수=${workbook.numberOfSheets}")

            return workbook
        } catch (e: Exception) {
            logger.error("Excel 템플릿 로드 중 오류 발생: $templatePath", e)
            throw CustomException(CustomErrorCode.UNKNOWN_SERVER_ERROR)
        }
    }

    /**
     * 빈 Workbook 생성
     */
    fun createEmptyWorkbook(): XSSFWorkbook {
        logger.debug("빈 Workbook 생성")
        return XSSFWorkbook()
    }
}
