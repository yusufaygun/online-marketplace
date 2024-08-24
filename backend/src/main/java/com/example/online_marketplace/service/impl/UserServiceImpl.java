package com.example.online_marketplace.service.impl;

import com.example.online_marketplace.dto.ProductDto;
import com.example.online_marketplace.dto.SellerDto;
import com.example.online_marketplace.dto.UserDto;
import com.example.online_marketplace.dto.UserInputDto;
import com.example.online_marketplace.exception.EntityAlreadyExistsException;
import com.example.online_marketplace.exception.EntityNotFoundException;
import com.example.online_marketplace.mapper.ProductMapper;
import com.example.online_marketplace.mapper.SellerMapper;
import com.example.online_marketplace.mapper.UserMapper;
import com.example.online_marketplace.model.Product;
import com.example.online_marketplace.model.Role;
import com.example.online_marketplace.model.Seller;
import com.example.online_marketplace.model.User;
import com.example.online_marketplace.repository.ProductRepository;
import com.example.online_marketplace.repository.RoleRepository;
import com.example.online_marketplace.repository.SellerRepository;
import com.example.online_marketplace.repository.UserRepository;
import com.example.online_marketplace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ProductRepository productRepository, SellerRepository sellerRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public UserDto saveUser(UserInputDto userInputDto) {
        User user = UserMapper.mapInputDtoToEntity(userInputDto);
        var existingUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        if(existingUser != null){
            throw new EntityAlreadyExistsException("User already exists with the given username.");
        }

        // Varsayılan olarak 'user' rolünü ata
        Role userRole = roleRepository.findByName("user").orElseThrow(() -> new EntityNotFoundException("role user is not defined yet"));
        user.getRoles().add(userRole);

        User savedUser = userRepository.save(user);
        return UserMapper.mapEntityToDto(savedUser);
    }

    @Override
    public UserDto saveAdmin(UserInputDto userInputDto) {
        User user = UserMapper.mapInputDtoToEntity(userInputDto);
        var existingUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        if(existingUser != null){
            throw new EntityAlreadyExistsException("User already exists with the given username.");
        }

        // 'admin' rolünü ata
        Role userRole = roleRepository.findByName("admin").orElseThrow(() -> new EntityNotFoundException("role user is not defined yet"));
        user.getRoles().add(userRole);

        User savedUser = userRepository.save(user);
        return UserMapper.mapEntityToDto(savedUser);
    }

    @Override
    public Page<UserDto> findAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageable);

        // Page<ProductDto>'ya dönüştürme
        List<UserDto> userDtos = userPage.stream()
                .map(UserMapper::mapEntityToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(userDtos, pageable, userPage.getTotalElements());
    }

    @Override
    public UserDto findById(Long id) {
        User user = getUserById(id);
        return UserMapper.mapEntityToDto(user);
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with the given username."));
        return UserMapper.mapEntityToDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UserInputDto userInputDto) {
        // Kullanıcıyı ID'ye göre bul
        User existingUser = getUserById(id);

        // Kullanıcıyı DTO'dan güncelle
        User updatedUser = UserMapper.mapInputDtoToEntity(userInputDto);

        // Kullanıcının mevcut rol ve diğer bilgilerini koru
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setName(updatedUser.getName());
        existingUser.setSurname(updatedUser.getSurname());
        existingUser.setPassword(updatedUser.getPassword());

        // Kullanıcıyı veritabanına kaydet
        User savedUser = userRepository.save(existingUser);

        // DTO olarak döndür
        return UserMapper.mapEntityToDto(savedUser);
    }


    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Override
    public UserDto assignRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with the given username."));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role not found."));
        user.getRoles().add(role);
        return UserMapper.mapEntityToDto(userRepository.save(user));
    }

    @Override
    public List<ProductDto> getFavoriteProducts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return user.getFavoriteProducts().stream()
                .map(ProductMapper::mapEntityToDto) // Favori ürünleri ProductDto'ya dönüştürüyoruz
                .collect(Collectors.toList());
    }


    @Override
    public UserDto addProductToFavorites(Long userId, Long productId) {
        User user = getUserById(userId);
        Product product = getProductById(productId);

        user.getFavoriteProducts().add(product);
        userRepository.save(user);

        return UserMapper.mapEntityToDto(user);
    }

    @Override
    public UserDto removeProductFromFavorites(Long userId, Long productId) {
        User user = getUserById(userId);
        Product product = getProductById(productId);

        user.getFavoriteProducts().remove(product);
        userRepository.save(user);

        return UserMapper.mapEntityToDto(user);
    }

    @Override
    public List<SellerDto> getBlacklist(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return user.getBlacklistedSellers().stream()
                .map(SellerMapper::mapEntityToDto) // Blacklist'teki Seller'ları SellerDto'ya dönüştürüyoruz
                .collect(Collectors.toList());
    }

    @Override
    public UserDto addSellerToBlacklist(Long userId, Long sellerId) {
        User user = getUserById(userId);
        Seller seller = getSellerById(sellerId);

        user.getBlacklistedSellers().add(seller);
        return UserMapper.mapEntityToDto(userRepository.save(user));
    }

    @Override
    public UserDto removeSellerFromBlacklist(Long userId, Long sellerId) {
        User user = getUserById(userId);
        Seller seller = getSellerById(sellerId);

        user.getBlacklistedSellers().remove(seller);
        return UserMapper.mapEntityToDto(userRepository.save(user));
    }


    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with the given ID."));
    }

    private Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with the given ID."));
    }

    private Seller getSellerById(Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seller not found with the given ID."));
    }
}
