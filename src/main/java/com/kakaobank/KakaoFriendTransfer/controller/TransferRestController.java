package com.kakaobank.KakaoFriendTransfer.controller;

import com.kakaobank.KakaoFriendTransfer.config.exception.GlobalException;
import com.kakaobank.KakaoFriendTransfer.domain.Transfer;
import com.kakaobank.KakaoFriendTransfer.domain.dto.TransferDto;
import com.kakaobank.KakaoFriendTransfer.mapper.TransferMapper;
import com.kakaobank.KakaoFriendTransfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transfer")
public class TransferRestController {
    private final TransferService transferService;
    private final TransferMapper transferMapper;

    @PostMapping
    public ResponseEntity createTransfer(@RequestBody TransferDto transferDto) throws GlobalException {
        Transfer transfer = transferMapper.dtoToNewEntity(transferDto);
        transferService.saveTransfer(transfer);

        return new ResponseEntity<>(transfer, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public TransferDto findTransfer(@PathVariable Long id) {
        Transfer transfer = transferService.findTransfer(id);
        return transferMapper.entityToDto(transfer);
//        return transferService.findTransferDto(id);
    }

    @GetMapping("/sender/{kakaoUserId}")
    public List<Transfer> findTransferBySender(@PathVariable String kakaoUserId) {
        return transferService.findTransferListBySender(kakaoUserId);
    }

    @GetMapping("/receiver/{kakaoUserId}")
    public List<Transfer> findTransferByReceiver(@PathVariable String kakaoUserId) {
        return transferService.findTransferListByReceiver(kakaoUserId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity cancelTransfer(@PathVariable Long id) throws GlobalException {

        Transfer result = transferService.cancelTransfer(id);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity confirmTransfer(@PathVariable Long id) throws GlobalException {
        Transfer transfer = new Transfer();
        transfer.setId(id);

        Transfer result = transferService.confirmTransfer(id);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
