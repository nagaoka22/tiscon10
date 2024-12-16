package com.tiscon10.form;

import org.hibernate.validator.constraints.Length;

import com.tiscon10.code.JobType;
import com.tiscon10.code.MarriedType;
import com.tiscon10.code.TreatedType;
import com.tiscon10.validator.CodeValue;
import com.tiscon10.validator.Numeric;
import com.tiscon10.validator.YYYYMMDD;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 顧客が入力した見積もり情報。
 * 
 * @author TIS Taro
 *
 * @param insuranceType  保険種別
 * @param kanjiName      氏名
 * @param kanaName       シメイ（フリガナ）
 * @param dateOfBirth    生年月日
 * @param address        住所
 * @param tel            電話番号
 * @param email          メールアドレス
 * @param marriedType    配偶者有無
 * @param jobType        ご職業
 * @param income         昨年の所得(万円)
 * @param treatedType    病歴有無
 * @param medicalHistory 病歴
 */
public record UserOrderForm(

    @NotBlank @Numeric
    String insuranceType,    // 保険種別

    @NotBlank @Size(max = 60)
    String kanjiName,    // 氏名

    @NotBlank @Pattern(regexp = "^[ァ-ヶー　]*$", message = "カタカナで入力してください") @Size(max = 90)
    String kanaName,    // シメイ（フリガナ）

    @NotBlank @YYYYMMDD()
    String dateOfBirth,    // 生年月日

    @NotBlank @Size(max = 255)
    String address,     // 住所

    @NotBlank @Numeric @Size(max = 13)
    String tel,          // 電話番号

    @NotBlank @Email @Size(max = 255)
    String email,        // メールアドレス

    @NotBlank @CodeValue(value = MarriedType.class)
    String marriedType,  // 配偶者有無

    @NotBlank @CodeValue(value = JobType.class)
    String jobType,     // ご職業

    @NotBlank @Numeric @Size(max = 9)
    String income,     // 昨年の所得(万円)

    @NotBlank @CodeValue(value = TreatedType.class)
    String treatedType,  // 病歴有無

    @Length(max = 120)
    String medicalHistory  // 病歴
) {

    /**
     * 病歴有無が「有」の場合、病歴が必須であることを検証する。
     *
     * @return 病歴有無が「有」かつ病歴が記入されてない場合、偽
     */
    @AssertTrue(message = "{tiscon.medicalHistoryEmpty.message}")
    public boolean isTreatedMedicalHistoryValid() {
        if (TreatedType.TREATED.hasCode(this.treatedType)) {
            return (this.medicalHistory != null && !this.medicalHistory.trim().isEmpty());
        }
        return true;
    }

    /**
     * 病歴有無が「無」の場合、病歴は記入されていないことを検証する。
     *
     * @return 病歴有無が「無」かつ病歴が記入されている場合、偽
     */
    @AssertTrue(message = "{tiscon.medicalHistoryNotRequired.message}")
    public boolean isUntreatedMedicalHistoryValid() {
        if (TreatedType.UNTREATED.hasCode(this.treatedType)) {
            return (this.medicalHistory == null || this.medicalHistory.trim().isEmpty());
        }
        return true;
    }

    /**
     * 配偶者有無の列挙型(enum)を取得する。
     *
     * @return 配偶者有無の列挙型
     */
    public MarriedType getMarriedTypeEnum() {
        return MarriedType.getEnumFromCode(this.marriedType);
    }

    /**
     * ご職業の列挙型(enum)を取得する。
     *
     * @return ご職業の列挙型
     */
    public JobType getJobTypeEnum() {
        return JobType.getEnumFromCode(this.jobType);
    }

    /**
     * 病歴有無の列挙型(enum)を取得する。
     *
     * @return 病歴有無の列挙型
     */
    public TreatedType getTreatedTypeEnum() {
        return TreatedType.getEnumFromCode(this.treatedType);
    }
}
