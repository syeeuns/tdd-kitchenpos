# 키친포스

## 요구 사항
### Product
- [ ] `Product`를 생성할 수 있다.
- [ ] `Product` 전체 목록을 조회할 수 있다.
- [ ] `name`은 반드시 들어가야한다. (`Exception`)
- [ ] `name`에는 욕설(profanity)이 들어가면 안된다. (`Exception`)

- [ ] `Product`의 `price`를 변경할 수 있다.
- [ ] `price`는 반드시 들어가야한다. (`Exception`)
- [ ] `price`는 0 이상이어야 한다. (`Exception`)

### Menu 
- [ ] `Menu`를 생성할 수 있다.
- [ ] `Menu` 전체 목록을 조회할 수 있다.
- [ ] `price`는 반드시 들어가야한다. (`Exception`)
- [ ] `price`는 0 이상이어야 한다. (`Exception`)
- [ ] `menuProduct`의 `quantity`는 0이상이어야 한다. (`Exception`)
- [ ] `menu`의 `price`는 `menuProduct`의 `price`*`quantity` 이하이어야 한다. (`Exception`)
- [ ] `name`은 반드시 들어가야한다. (`Exception`)
- [ ] `name`에는 욕설(profanity)이 들어가면 안된다. (`Exception`)

- [ ] `Menu`의 가격을 변경할 수 있다.
- [ ] `price`는 반드시 존재해야한다. (`Exception`)
- [ ] `price`는 0 이상이어야 한다. (`Exception`)
- [ ] `menu`의 `price`는 `menuProduct`의 `price`*`quantity` 이하이어야 한다. (`Exception`)
- [ ] `displayed`의 상태를 변경할 수 있다.

### Menu-groups 
- [ ] `Menu-groups`를 생성할 수 있다.
- [ ] `Menu-groups` 전체 목록을 조회할 수 있다.
- [ ] `name`은 반드시 존재해야한다. (`Exception`)

### Order
- [ ] `Order`를 생성할 수 있다.
- [ ] `Order`전체 목록을 조회할 수 있다.
- [ ] `Order`의 `status`를 변경할 수 있다.
- [ ] `type`은 반드시 존재해야한다. (`Exception`)
- [ ] `type`은 `DELIVERY`, `TAKEOUT`, `EAT_IN` 중 하나이어야한다.
- [ ] `type`이 `EAT_IN`인 경우, `orderLineItem`의 `quantity`는 0 이상이어야 한다. (`Exception`)
- [ ] `oderLineItem`의 `menuId`에 해당하는 `Menu`는 `displayed`가 `true`여야한다. (`Exception`)
- [ ] `oderLineItem`의 `price`는 `menuId`에 해당하는 `Menu`의 `price`와 같아야한다. (`Exception`)
- [ ] `type`이 `DELIVERY`인 경우, `deliveryAddress`가 있어야한다. (`Exception`)
- [ ] `type`이 `EAT_IN`인 경우, `orderTableId`에 해당하는 `OrderTable`은 `empty`가 `true`이면 안된다. (`Exception`)
- [ ] `Order`의 `status`를 `ACCEPTED`로 변경할 때에는, `status`는 `WAITING`이어야 한다. (`Exception`)
- [ ] `Order`의 `status`를 `SERVED`로 변경할 때에는, `status`는 `ACCEPTED`이어야 한다. (`Exception`)
- [ ] `Order`의 `status`를 `DELIVIERING`로 변경할 때에는, `type`는 `DELIVERY`이어야 한다. (`Exception`)
- [ ] `Order`의 `status`를 `DELIVIERING`로 변경할 때에는, `status`는 `SERVED`이어야 한다. (`Exception`)
- [ ] `Order`의 `status`를 `DELIVERED`로 변경할 때에는, `type`는 `DELIVERY`이어야 한다. (`Exception`)
- [ ] `Order`의 `status`를 `DELIVERED`로 변경할 때에는, `status`는 `DELIVERING`이어야 한다. (`Exception`)
- [ ] `Order`의 `status`를 `COMPLETED`로 변경할 때, `Order`의 `type`이 `DELIVERY`라면, `status`가 `DELIVERED`이어야 한다. (`Exception`)
- [ ] `Order`의 `status`를 `COMPLETED`로 변경할 때, `Order`의 `type`이 `DELIVERY`가 아니면, `status`가 `SERVED`이어야 한다. (`Exception`)

### Order-table
- [ ] `Order-table`을 생성할 수 있다.
- [ ] `Order-table`의 `empty`의 상태를 변경할 수 있다.
- [ ] `Order-table`의 `numberOfGeust`를 변경할 수 있다.
- [ ] `name`은 반드시 들어가야한다. (`Exception`)
- [ ] `Order-table`의 `empty`를 `true`로 변경할 때, `orderTableId`에 해당하는 `Order-table`의 `status`는 `COMPLETED`이어야 한다. (`Exception`)
- [ ] `Order-table`의 `numberOfGuest`를 변경할 때, `orderTableId`에 해당하는 `Order-table`의 `empty`는 `false`이어야 한다. (`Exception`)
- [ ] `Order-table`의 `numberOfGuest`를 변경할 때, `numberOfGuests`는 0 이상이어야 한다. (`Exception`)
  

## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
|  |  |  |

## 모델링
