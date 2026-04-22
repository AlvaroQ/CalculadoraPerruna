package com.alvaroquintana.edadperruna.core.data.local.entity

import com.alvaroquintana.edadperruna.core.domain.model.Dog
import com.alvaroquintana.edadperruna.core.domain.model.FCI
import com.alvaroquintana.edadperruna.core.domain.model.Height
import com.alvaroquintana.edadperruna.core.domain.model.LifeExpectancy
import com.alvaroquintana.edadperruna.core.domain.model.MainInformation
import com.alvaroquintana.edadperruna.core.domain.model.PhysicalCharacteristics
import com.alvaroquintana.edadperruna.core.domain.model.Prize
import com.alvaroquintana.edadperruna.core.domain.model.Weight
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DogEntityMapperTest {

    @Test
    fun `toDomain decodes a fully populated entity`() {
        val entity = DogEntity(
            breedId = "labrador",
            name = "Labrador Retriever",
            life = "12-13 years",
            image = "https://img/lab.jpg",
            otherNames = """["Lab","Labrador"]""",
            shortDescription = "Friendly and outgoing",
            lifeExpectancy = 12,
            lifeExpectancyMeasure = "years",
            character = """["Friendly","Active"]""",
            sizeBreed = "Large",
            typeHair = """["Short","Dense"]""",
            typeHairDescription = "Double coat",
            prizeCost = """[800,1500]""",
            prizeCurrency = "EUR",
            fciGroup = 8,
            fciGroupType = "Retrievers",
            fciSection = 1,
            fciSectionType = "Retrievers",
            weightMacho = """[29,36]""",
            weightHembra = """[25,32]""",
            weightMedida = "kg",
            heightMacho = """[57,62]""",
            heightHembra = """[55,60]""",
            heightMedida = "cm",
            colorHair = "Black, Yellow, Chocolate",
            physicalDescription = "Strong, athletic",
            commonDiseases = """["Hip dysplasia","Obesity"]""",
            hygiene = "Weekly brushing",
            lossHair = "Moderate",
            nutrition = "High protein",
        )

        val dog = entity.toDomain()

        assertEquals("labrador", dog.breedId)
        assertEquals("Labrador Retriever", dog.name)
        assertEquals(listOf("Lab", "Labrador"), dog.otherNames)
        assertEquals(12, dog.mainInformation?.lifeExpectancy?.expectancy)
        assertEquals("years", dog.mainInformation?.lifeExpectancy?.measure)
        assertEquals(listOf("Friendly", "Active"), dog.mainInformation?.character)
        assertEquals(listOf(800, 1500), dog.mainInformation?.prize?.cost)
        assertEquals(8L, dog.fci?.group)
        assertEquals("Retrievers", dog.fci?.groupType)
        assertEquals(listOf(29, 36), dog.physicalCharacteristics?.weight?.macho)
        assertEquals(listOf(25, 32), dog.physicalCharacteristics?.weight?.hembra)
        assertEquals("kg", dog.physicalCharacteristics?.weight?.medida)
        assertEquals(listOf(57, 62), dog.physicalCharacteristics?.height?.macho)
        assertEquals("Black, Yellow, Chocolate", dog.physicalCharacteristics?.colorHair)
        assertEquals(listOf("Hip dysplasia", "Obesity"), dog.commonDiseases)
        assertEquals("Weekly brushing", dog.hygiene)
    }

    @Test
    fun `toDomain handles entity with default empty values`() {
        val entity = DogEntity(breedId = "empty")

        val dog = entity.toDomain()

        assertEquals("empty", dog.breedId)
        assertEquals("", dog.name)
        assertEquals(emptyList<String>(), dog.otherNames)
        assertEquals(emptyList<String>(), dog.mainInformation?.character)
        assertEquals(emptyList<Int>(), dog.mainInformation?.prize?.cost)
        assertEquals(0, dog.mainInformation?.lifeExpectancy?.expectancy)
        assertEquals(0L, dog.fci?.group)
        assertEquals(emptyList<Int>(), dog.physicalCharacteristics?.weight?.macho)
        assertEquals(emptyList<String>(), dog.commonDiseases)
    }

    @Test
    fun `toEntity encodes a fully populated dog`() {
        val dog = Dog(
            breedId = "poodle",
            name = "Poodle",
            life = "10-18 years",
            image = "https://img/poodle.jpg",
            otherNames = listOf("Caniche"),
            shortDescription = "Smart and elegant",
            mainInformation = MainInformation(
                lifeExpectancy = LifeExpectancy(14, "years"),
                character = listOf("Intelligent", "Active"),
                sizeBreed = "Medium",
                typeHair = listOf("Curly"),
                typeHairDescription = "Hypoallergenic",
                prize = Prize(listOf(1000, 2500), "EUR"),
            ),
            fci = FCI(9, "Companion", 2, "Poodle"),
            physicalCharacteristics = PhysicalCharacteristics(
                weight = Weight(listOf(20, 32), listOf(20, 27), "kg"),
                height = Height(listOf(45, 60), listOf(45, 60), "cm"),
                colorHair = "Black, White, Apricot",
                description = "Athletic build",
            ),
            commonDiseases = listOf("Addison's disease"),
            hygiene = "Regular grooming",
            lossHair = "Low",
            nutrition = "Balanced",
        )

        val entity = dog.toEntity()

        assertEquals("poodle", entity.breedId)
        assertEquals("Poodle", entity.name)
        assertEquals("""["Caniche"]""", entity.otherNames)
        assertEquals(14, entity.lifeExpectancy)
        assertEquals("years", entity.lifeExpectancyMeasure)
        assertEquals("""["Intelligent","Active"]""", entity.character)
        assertEquals("""[1000,2500]""", entity.prizeCost)
        assertEquals("EUR", entity.prizeCurrency)
        assertEquals(9L, entity.fciGroup)
        assertEquals("Companion", entity.fciGroupType)
        assertEquals("""[20,32]""", entity.weightMacho)
        assertEquals("""[20,27]""", entity.weightHembra)
        assertEquals("kg", entity.weightMedida)
        assertEquals("""[45,60]""", entity.heightMacho)
        assertEquals("""["Addison's disease"]""", entity.commonDiseases)
    }

    @Test
    fun `toEntity uses safe defaults for null nested fields`() {
        val dog = Dog(
            breedId = "bare",
            name = "Bare Dog",
            mainInformation = null,
            fci = null,
            physicalCharacteristics = null,
        )

        val entity = dog.toEntity()

        assertEquals("bare", entity.breedId)
        assertEquals(0, entity.lifeExpectancy)
        assertEquals("", entity.lifeExpectancyMeasure)
        assertEquals("[]", entity.character)
        assertEquals("[]", entity.typeHair)
        assertEquals("[]", entity.prizeCost)
        assertEquals("", entity.prizeCurrency)
        assertEquals(0L, entity.fciGroup)
        assertEquals("", entity.fciGroupType)
        assertEquals("[]", entity.weightMacho)
        assertEquals("[]", entity.weightHembra)
        assertEquals("", entity.weightMedida)
        assertEquals("[]", entity.heightMacho)
        assertEquals("", entity.colorHair)
    }

    @Test
    fun `toEntity then toDomain round-trips a fully populated dog`() {
        val original = Dog(
            breedId = "german-shepherd",
            name = "German Shepherd",
            life = "9-13 years",
            image = "https://img/gsd.jpg",
            otherNames = listOf("GSD", "Alsatian"),
            shortDescription = "Loyal and intelligent",
            mainInformation = MainInformation(
                lifeExpectancy = LifeExpectancy(11, "years"),
                character = listOf("Loyal", "Confident", "Courageous"),
                sizeBreed = "Large",
                typeHair = listOf("Medium", "Double"),
                typeHairDescription = "Weather resistant",
                prize = Prize(listOf(500, 1500), "EUR"),
            ),
            fci = FCI(1, "Sheepdogs", 1, "Sheepdogs"),
            physicalCharacteristics = PhysicalCharacteristics(
                weight = Weight(listOf(30, 40), listOf(22, 32), "kg"),
                height = Height(listOf(60, 65), listOf(55, 60), "cm"),
                colorHair = "Black & Tan",
                description = "Strong, agile",
            ),
            commonDiseases = listOf("Hip dysplasia", "Bloat"),
            hygiene = "Brush 2-3 times/week",
            lossHair = "High",
            nutrition = "High quality kibble",
        )

        val roundTripped = original.toEntity().toDomain()

        assertEquals(original.breedId, roundTripped.breedId)
        assertEquals(original.name, roundTripped.name)
        assertEquals(original.otherNames, roundTripped.otherNames)
        assertEquals(original.mainInformation?.character, roundTripped.mainInformation?.character)
        assertEquals(original.mainInformation?.prize?.cost, roundTripped.mainInformation?.prize?.cost)
        assertEquals(original.fci?.group, roundTripped.fci?.group)
        assertEquals(original.fci?.sectionType, roundTripped.fci?.sectionType)
        assertEquals(original.physicalCharacteristics?.weight?.macho, roundTripped.physicalCharacteristics?.weight?.macho)
        assertEquals(original.physicalCharacteristics?.weight?.hembra, roundTripped.physicalCharacteristics?.weight?.hembra)
        assertEquals(original.physicalCharacteristics?.height?.macho, roundTripped.physicalCharacteristics?.height?.macho)
        assertEquals(original.commonDiseases, roundTripped.commonDiseases)
    }

    @Test
    fun `round-trip preserves empty collections`() {
        val original = Dog(
            breedId = "minimal",
            otherNames = emptyList(),
            mainInformation = MainInformation(
                character = emptyList(),
                typeHair = emptyList(),
                prize = Prize(emptyList(), ""),
            ),
            physicalCharacteristics = PhysicalCharacteristics(
                weight = Weight(emptyList(), emptyList(), ""),
                height = Height(emptyList(), emptyList(), ""),
            ),
            commonDiseases = emptyList(),
        )

        val roundTripped = original.toEntity().toDomain()

        assertEquals(emptyList<String>(), roundTripped.otherNames)
        assertTrue(roundTripped.mainInformation?.character?.isEmpty() == true)
        assertTrue(roundTripped.mainInformation?.prize?.cost?.isEmpty() == true)
        assertTrue(roundTripped.physicalCharacteristics?.weight?.macho?.isEmpty() == true)
        assertTrue(roundTripped.commonDiseases.isEmpty())
    }

    @Test
    fun `toDomain always produces non-null nested objects`() {
        val entity = DogEntity(breedId = "x")

        val dog = entity.toDomain()

        assertNotNull(dog.mainInformation)
        assertNotNull(dog.fci)
        assertNotNull(dog.physicalCharacteristics)
        assertNotNull(dog.mainInformation?.lifeExpectancy)
        assertNotNull(dog.mainInformation?.prize)
        assertNotNull(dog.physicalCharacteristics?.weight)
        assertNotNull(dog.physicalCharacteristics?.height)
    }
}
