package com.lash.lashClone.controller.api;


import com.lash.lashClone.aop.annotation.LogAspect;
import com.lash.lashClone.aop.annotation.ValidAspect;
import com.lash.lashClone.dto.CMRespDto;
import com.lash.lashClone.dto.account.AddressReqDto;
import com.lash.lashClone.dto.account.MyPageOrderRespDto;
import com.lash.lashClone.dto.account.RegisterReqDto;
import com.lash.lashClone.dto.account.UserUpdateReqDto;
import com.lash.lashClone.dto.validation.ValidationSequence;
import com.lash.lashClone.service.AccountService;
import com.lash.lashClone.service.mypage.AddressService;
import com.lash.lashClone.service.mypage.MyPageOrderService;
import com.lash.lashClone.service.mypage.MyPageService;

import com.lash.lashClone.service.auth.PrincipalDetails;
import com.lash.lashClone.service.mypage.UserService;
import com.lash.lashClone.service.mypage.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/account")
@RestController
@RequiredArgsConstructor
public class AccountApi {
    private final AccountService accountService;
    private final AddressService addressService;
    private final MyPageService myPageService;
    private final UserService userService;
    private final MyPageOrderService myPageOrderService;


    @LogAspect
    @ValidAspect
    @PostMapping("/join")
    public ResponseEntity<?> register(@Validated(ValidationSequence.class) @RequestBody RegisterReqDto registerReqDto, BindingResult bindingResult) throws Exception {

        accountService.checkDuplicateEmail(registerReqDto.getUsername());
        accountService.join(registerReqDto);

        return ResponseEntity.ok().body(new CMRespDto<>(1, "Successfully join", registerReqDto));
    }
    @PostMapping("/shippingAddressRegistration")
    public ResponseEntity<?> addAddress(AddressReqDto addressReqDto, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        String username = principalDetails.getMember().getUsername();

        return ResponseEntity.created(null)
                .body(new CMRespDto<>(1, "success", addressService.addAddress(addressReqDto, username)));
    }
    //-------------?????? ?????? ??????--------------
    @DeleteMapping("/user/{memberId}")
    public ResponseEntity<?> deleteUser(@PathVariable int memberId) throws Exception {
//        principalDetails.getMember().setmemberId(userUpdateReqDto.getmemberId());
        return ResponseEntity.ok(new CMRespDto<>(1, "success", userService.deleteUser(memberId)));
    }
    // ----------?????? ?????? ??????-------
    @PutMapping("/user/update")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateReqDto userUpdateReqDto, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
//        principalDetails.getMember().setphone(userUpdateReqDto.getphone());
        System.out.println("update");
        System.out.println(principalDetails.getMember());
        System.out.println(userUpdateReqDto);

        return ResponseEntity.ok(new CMRespDto<>(1, "success", userService.updateUser(userUpdateReqDto, principalDetails)));
    }
    //---------?????? ?????????-------------
    @GetMapping("/user")
    public ResponseEntity<?> getuser(@AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        System.out.println(principalDetails.getMember().getUsername());
        return ResponseEntity.ok(new CMRespDto<>(1, "success", userService.getUser(principalDetails.getMember().getUsername())));
    }

    // ----------???????????????------------
    @GetMapping("/mypage")
    public ResponseEntity<?> myPage(@AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        return ResponseEntity.ok(new CMRespDto<>(1, "success", myPageService.memberList(principalDetails.getMember().getUsername())));
    }

    @GetMapping("/mypage/count")
    public ResponseEntity<?> myPageOrder(@AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        return ResponseEntity.ok(new CMRespDto<>(1, "success", myPageService.getOrderCount(principalDetails.getMember().getUsername())));
    }

    // --------????????? ??????-----------
    @GetMapping("/shippingAddress")
    public ResponseEntity<?> addressList(@AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {

        return ResponseEntity.ok(new CMRespDto<>(1, "success", addressService.addressList(principalDetails.getMember().getUsername())));

    }
    // --------????????? ??????-----------
    @DeleteMapping("/shippingAddress/{addressId}")
    public ResponseEntity<?> deleteProduct(@PathVariable int addressId) throws Exception {

        return ResponseEntity.ok(new CMRespDto<>(1, "success", addressService.deleteAddress(addressId)));
    }
    //-----------????????????-----------------
    @GetMapping("/mypage/order")
    public ResponseEntity<?> getMyPageOrder(@AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
//            System.out.println(principalDetails.getMember().getUsername());
//        myPageOrderService.getMyPageOrder(principalDetails.getMember().getUsername())
        return ResponseEntity.ok(new CMRespDto<>(1, "success", myPageOrderService.getMyPageOrder(principalDetails.getMember().getUsername())));
    }
    @GetMapping("/principal/member")
    public ResponseEntity<?> getPrincipalMember(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok().body(new CMRespDto<>(1, "Successfully get principal", principalDetails == null ? "" : principalDetails));
    }



}
