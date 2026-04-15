package com.alvaroquintana.edadperruna.core.data.local.entity

import com.alvaroquintana.edadperruna.core.domain.model.Dog
import com.alvaroquintana.edadperruna.core.domain.model.FCI
import com.alvaroquintana.edadperruna.core.domain.model.Height
import com.alvaroquintana.edadperruna.core.domain.model.LifeExpectancy
import com.alvaroquintana.edadperruna.core.domain.model.MainInformation
import com.alvaroquintana.edadperruna.core.domain.model.PhysicalCharacteristics
import com.alvaroquintana.edadperruna.core.domain.model.Prize
import com.alvaroquintana.edadperruna.core.domain.model.Weight
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

fun DogEntity.toDomain(): Dog = Dog(
    breedId = breedId,
    name = name,
    life = life,
    image = image,
    otherNames = json.decodeFromString<List<String>>(otherNames),
    shortDescription = shortDescription,
    mainInformation = MainInformation(
        lifeExpectancy = LifeExpectancy(lifeExpectancy, lifeExpectancyMeasure),
        character = json.decodeFromString<List<String>>(character),
        sizeBreed = sizeBreed,
        typeHair = json.decodeFromString<List<String>>(typeHair),
        typeHairDescription = typeHairDescription,
        prize = Prize(json.decodeFromString<List<Int>>(prizeCost), prizeCurrency),
    ),
    fci = FCI(fciGroup, fciGroupType, fciSection, fciSectionType),
    physicalCharacteristics = PhysicalCharacteristics(
        weight = Weight(json.decodeFromString<List<Int>>(weightMacho), json.decodeFromString<List<Int>>(weightHembra), weightMedida),
        height = Height(json.decodeFromString<List<Int>>(heightMacho), json.decodeFromString<List<Int>>(heightHembra), heightMedida),
        colorHair = colorHair,
        description = physicalDescription,
    ),
    commonDiseases = json.decodeFromString<List<String>>(commonDiseases),
    hygiene = hygiene,
    lossHair = lossHair,
    nutrition = nutrition,
)

fun Dog.toEntity(): DogEntity = DogEntity(
    breedId = breedId,
    name = name,
    life = life,
    image = image,
    otherNames = json.encodeToString(otherNames),
    shortDescription = shortDescription,
    lifeExpectancy = mainInformation?.lifeExpectancy?.expectancy ?: 0,
    lifeExpectancyMeasure = mainInformation?.lifeExpectancy?.measure ?: "",
    character = json.encodeToString(mainInformation?.character ?: emptyList<String>()),
    sizeBreed = mainInformation?.sizeBreed ?: "",
    typeHair = json.encodeToString(mainInformation?.typeHair ?: emptyList<String>()),
    typeHairDescription = mainInformation?.typeHairDescription ?: "",
    prizeCost = json.encodeToString(mainInformation?.prize?.cost ?: emptyList<Int>()),
    prizeCurrency = mainInformation?.prize?.currency ?: "",
    fciGroup = fci?.group ?: 0,
    fciGroupType = fci?.groupType ?: "",
    fciSection = fci?.section ?: 0,
    fciSectionType = fci?.sectionType ?: "",
    weightMacho = json.encodeToString(physicalCharacteristics?.weight?.macho ?: emptyList<Int>()),
    weightHembra = json.encodeToString(physicalCharacteristics?.weight?.hembra ?: emptyList<Int>()),
    weightMedida = physicalCharacteristics?.weight?.medida ?: "",
    heightMacho = json.encodeToString(physicalCharacteristics?.height?.macho ?: emptyList<Int>()),
    heightHembra = json.encodeToString(physicalCharacteristics?.height?.hembra ?: emptyList<Int>()),
    heightMedida = physicalCharacteristics?.height?.medida ?: "",
    colorHair = physicalCharacteristics?.colorHair ?: "",
    physicalDescription = physicalCharacteristics?.description ?: "",
    commonDiseases = json.encodeToString(commonDiseases),
    hygiene = hygiene,
    lossHair = lossHair,
    nutrition = nutrition,
)
