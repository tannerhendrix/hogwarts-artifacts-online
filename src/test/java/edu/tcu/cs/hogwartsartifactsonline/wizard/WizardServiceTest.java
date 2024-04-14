package edu.tcu.cs.hogwartsartifactsonline.wizard;

import jakarta.inject.Inject;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {
    @Mock
    WizardRepository wizardRepository;
    @InjectMocks
    WizardService wizardService;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        //Given. Arrange inputs and targets. Define the behavior of Mock object wizardRepository
        /*
           "id": 1,
           "name": "Albus Dumbledore",
           "numberOfArtifacts": 2
*/
        Wizard w = new Wizard();
        w.setId(1);
        w.setName("Albus Dumbledore");

        given(this.wizardRepository.findById(1)).willReturn(Optional.of(w));

        //When. Act on the target behavior. When steps should cover the method to be tested
        Wizard returnedWizard = wizardService.findById(1);

        //Then. Assert expected outcomes
        assertThat(returnedWizard.getId()).isEqualTo(w.getId());
        assertThat(returnedWizard.getName()).isEqualTo(w.getName());
        verify(wizardRepository, times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound(){
        //Given
        given(wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        //When
        Throwable thrown = catchThrowable(() -> {
            Wizard returnedWizard = this.wizardService.findById(1);
        });

        //Then
        assertThat(thrown)
                .isInstanceOf(WizardNotFoundException.class)
                .hasMessage("Could not find wizard with Id 1 :(");
        verify(this.wizardRepository, times(1)).findById(1);
    }
    @Test
    void testSaveSuccess(){
        //Given
        Wizard newWizard = new Wizard();
        newWizard.setName("Hermione Granger");

        given(this.wizardRepository.save(newWizard)).willReturn(newWizard);

        //When
        Wizard returnedWizard = this.wizardService.save(newWizard);

        //Then
        assertThat(returnedWizard.getName()).isEqualTo(newWizard.getName());
        verify(this.wizardRepository, times(1)).save(newWizard);
    }
    @Test
    void testUpdateSuccess(){
        //Given
        Wizard oldWizard = new Wizard();
        oldWizard.setId(1);
        oldWizard.setName("Albus Dumbledore");

        Wizard update = new Wizard();
        update.setName("Albus Dumbledore - update");

        given(this.wizardRepository.findById(1)).willReturn(Optional.of(oldWizard));
        given(this.wizardRepository.save(oldWizard)).willReturn(oldWizard);
        //When
        Wizard updatedWizard = this.wizardService.update(1, update);

        //Then
        assertThat(updatedWizard.getId()).isEqualTo(1);
        assertThat(updatedWizard.getName()).isEqualTo(update.getName());
        verify(this.wizardRepository, times(1)).findById(1);
        verify(this.wizardRepository, times(1)).save(oldWizard);

    }
    @Test
    void testUpdateNotFound(){
        //Given
        Wizard update = new Wizard();
        update.setName("Albus Dumbledore - update");

        given(this.wizardRepository.findById(1)).willReturn(Optional.empty());

        //When
        assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.update(1, update);
        });

        //Then
        verify(this.wizardRepository, times(1)).findById(1);
    }
    @Test
    void testDeleteSuccess(){
        //Given
        Wizard w = new Wizard();
        w.setId(1);
        w.setName("Albus Dumbledore");

        given(this.wizardRepository.findById(1)).willReturn(Optional.of(w));
        doNothing().when(this.wizardRepository).deleteById(1);

        //When
        this.wizardService.delete(1);

        //Then
        verify(this.wizardRepository, times(1)).deleteById(1);
    }
    @Test
    void testDeleteNotFound(){
        //Given
        given(this.wizardRepository.findById(1)).willReturn(Optional.empty());

        //When
        assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.delete(1);
        });

        //Then
        verify(this.wizardRepository, times(1)).findById(1);
    }

}