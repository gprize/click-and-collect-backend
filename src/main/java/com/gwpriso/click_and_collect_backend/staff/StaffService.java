package com.gwpriso.click_and_collect_backend.staff;

import com.gwpriso.click_and_collect_backend.common.exception.EmailDejaUtiliseException;
import com.gwpriso.click_and_collect_backend.common.exception.EntityNotFoundException;
import com.gwpriso.click_and_collect_backend.magasin.Magasin;
import com.gwpriso.click_and_collect_backend.magasin.MagasinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffService {

    private final UtilisateurStaffRepository staffRepository;
    private final MagasinRepository magasinRepository;
    private final PasswordEncoder passwordEncoder;

    public List<StaffResponse> findByMagasin(UUID magasinId) {
        return staffRepository.findByMagasinId(magasinId).stream()
                .map(StaffResponse::from)
                .toList();
    }

    public StaffResponse create(UUID magasinId, StaffRequest request) {
        if (staffRepository.existsByEmailAndMagasinId(request.email(), magasinId)) {
            throw new EmailDejaUtiliseException("Email déjà utilisé pour ce magasin : " + request.email());
        }

        Magasin magasin = magasinRepository.findById(magasinId)
                .orElseThrow(() -> new EntityNotFoundException("Magasin introuvable : " + magasinId));

        UtilisateurStaff staff = new UtilisateurStaff();
        staff.setMagasin(magasin);
        staff.setEmail(request.email());
        staff.setMotDePasseHash(passwordEncoder.encode(request.motDePasse()));
        staff.setRole(request.role());

        return StaffResponse.from(staffRepository.save(staff));
    }

    public void delete(UUID magasinId, UUID staffId) {
        UtilisateurStaff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new EntityNotFoundException("Compte introuvable : " + staffId));

        if (!staff.getMagasin().getId().equals(magasinId)) {
            throw new EntityNotFoundException("Compte introuvable : " + staffId);
        }

        staffRepository.delete(staff);
    }
}