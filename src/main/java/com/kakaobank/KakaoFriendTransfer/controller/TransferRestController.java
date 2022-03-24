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
        Transfer transfer = transferMapper.dtoToEntity(transferDto);
        transferService.saveTransfer(transfer);

        return new ResponseEntity<>(transferDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public TransferDto findTransfer(@PathVariable Long id) {
        return transferService.findTransfer(id);
    }

    @GetMapping("/sender/{kakaoUserId}")
    public List<TransferDto> findTransferBySender(@PathVariable String kakaoUserId) {
        return transferService.findTransferListBySender(kakaoUserId);
    }

    @GetMapping("/receiver/{kakaoUserId}")
    public List<TransferDto> findTransferByReceiver(@PathVariable String kakaoUserId) {
        return transferService.findTransferListByReceiver(kakaoUserId);
    }

    @DeleteMapping
    public ResponseEntity cancelTransfer(@RequestBody TransferDto transferDto) throws GlobalException {
        Transfer transfer = transferMapper.dtoToEntity(transferDto);
        transferService.cancelTransfer(transfer);

        return new ResponseEntity<>(transferDto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity confirmTransfer(@RequestBody TransferDto transferDto) throws GlobalException {
        Transfer transfer = transferMapper.dtoToEntity(transferDto);
        transferService.cancelTransfer(transfer);

        return new ResponseEntity<>(transferDto, HttpStatus.OK);
    }
}
