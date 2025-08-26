package newangle.xagent.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import newangle.xagent.domain.user.User;
import newangle.xagent.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    private User userTest;
    private Long userId = 1L;
    @BeforeEach
    void setUp() {
        userTest = new User();
        userTest.setId(userId);
        userTest.setName("Name");
        userTest.setEmail("email@email.com");
        userTest.setPassword("password");
        userTest.setPhoneNumber("123456789");
    }

    @Test
    @DisplayName("Should change user infos")
    void updateUser() {
        // ========== Arrange  ==========
        
        Long userId = 1L;

        User updatedInfo = new User();
        updatedInfo.setName("New Name");
        updatedInfo.setEmail("newemail@email.com");
        updatedInfo.setPassword("newpassword");
        updatedInfo.setPhoneNumber("987654321");
        
        when(repository.getReferenceById(userId)).thenReturn(userTest);

        // Quando o método `save()` for chamado com QUALQUER objeto do tipo User,
        // ele deve retornar o mesmo objeto que foi passado como argumento.
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));


        // ========== 2. Act ==========

        User resultUser = userService.updateUser(userId, updatedInfo);


        // ========== 3. Assert ==========

        assertNotNull(resultUser);

        assertEquals(userId, resultUser.getId()); // O ID deve permanecer o mesmo
        assertEquals("New Name", resultUser.getName());
        assertEquals("newemail@email.com", resultUser.getEmail());
        assertEquals("newpassword", resultUser.getPassword());
        assertEquals("987654321", resultUser.getPhoneNumber());

        // Verifica se os métodos do repositório foram chamados o número esperado de vezes
        verify(repository, times(1)).getReferenceById(userId); // Verifica se a busca foi chamada 1 vez
        verify(repository, times(1)).save(userTest);      // Verifica se o save foi chamado 1 vez com o objeto `userTest` (que foi modificado)
    }
}