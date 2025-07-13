/*
 * Numismatics
 * Copyright (c) 2024 The Railways Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.ithundxr.createnumismatics.compat.computercraft.implementation.peripherals;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import dev.ithundxr.createnumismatics.Numismatics;
import dev.ithundxr.createnumismatics.config.NumismaticsConfig;
import dev.ithundxr.createnumismatics.content.backend.BankAccount;
import dev.ithundxr.createnumismatics.content.backend.IDeductable;
import dev.ithundxr.createnumismatics.content.backend.ReasonHolder;
import dev.ithundxr.createnumismatics.content.backend.sub_authorization.Authorization;
import dev.ithundxr.createnumismatics.content.backend.sub_authorization.SubAccount;
import dev.ithundxr.createnumismatics.content.bank.BankTerminalBlockEntity;
import net.minecraft.network.chat.Component;

import java.util.*;

public class BankTerminalPeripheral implements GenericPeripheral {

    @LuaFunction
    public final MethodResult getAccounts(BankTerminalBlockEntity terminal) throws LuaException {
        try {
            List<String> output = new ArrayList<String>();
            for (UUID uuid : Numismatics.BANK.accounts.keySet()) {
                output.add(uuid.toString());
            }
            return MethodResult.of(output);
        }
        catch (Exception ex){
            throw new LuaException("Accounts unavailable");
        }
    }

    @LuaFunction
    public final String getAccountLabel(BankTerminalBlockEntity terminal, String accountID) throws LuaException {
        UUID account$;
        try {
            account$ = UUID.fromString(accountID);
        } catch (IllegalArgumentException e) {
            throw new LuaException("Invalid UUID");
        }

        BankAccount bankAccount = Numismatics.BANK.getAccount(account$);
        if (bankAccount == null) {
            throw new LuaException("Account not found");
        }

        return bankAccount.getDisplayName().getString();
    }

    @LuaFunction
    public final boolean isPlayerOwned(BankTerminalBlockEntity terminal, String accountID) throws LuaException {
        UUID account$;
        try {
            account$ = UUID.fromString(accountID);
        } catch (IllegalArgumentException e) {
            throw new LuaException("Invalid UUID");
        }

        BankAccount bankAccount = Numismatics.BANK.getAccount(account$);
        if (bankAccount == null) {
            throw new LuaException("Account not found");
        }

        return (bankAccount.type == BankAccount.Type.PLAYER);
    }

    @LuaFunction
    public final int getBalance(BankTerminalBlockEntity terminal, String accountID) throws LuaException {
        UUID account$;
        try {
            account$ = UUID.fromString(accountID);
        } catch (IllegalArgumentException e) {
            throw new LuaException("Invalid UUID");
        }

        BankAccount bankAccount = Numismatics.BANK.getAccount(account$);
        if (bankAccount == null) {
            throw new LuaException("Account not found");
        }

        return bankAccount.getBalance();
    }

    @LuaFunction
    public final List<String> getSubAccounts(BankTerminalBlockEntity terminal, String accountID) throws LuaException {
        if (NumismaticsConfig.server().getSubAccountsCommand.get()) {
            UUID account$;
            try {
                account$ = UUID.fromString(accountID);
            } catch (IllegalArgumentException e) {
                throw new LuaException("Invalid UUID");
            }

            BankAccount bankAccount = Numismatics.BANK.getAccount(account$);
            if (bankAccount == null) {
                throw new LuaException("Account not found");
            }

            List<String> output = new ArrayList<String>();
            bankAccount.getSubAccounts().forEach((temp) -> {
                output.add(temp.getAuthorizationID().toString());
            });

            if (output.isEmpty()) {
                throw new LuaException("No sub accounts");
            }
            return output;
        } else {
            throw new LuaException("Function disabled by config");
        }
    }

    @LuaFunction
    public final String getSubAccountLabel(BankTerminalBlockEntity terminal, String accountID, String authorizationID) throws LuaException {
        UUID account$, authorization$;
        try {
            account$ = UUID.fromString(accountID);
            authorization$ = UUID.fromString(authorizationID);
        } catch (IllegalArgumentException e) {
            throw new LuaException("Invalid UUID");
        }

        Authorization authorization = new Authorization.Anonymous(authorization$);

        BankAccount bankAccount = Numismatics.BANK.getAccount(account$);
        if (bankAccount == null) {
            throw new LuaException("Account not found");
        }

        ReasonHolder reasonHolder = new ReasonHolder();
        SubAccount subAccount = bankAccount.getSubAccount(authorization, reasonHolder);

        if (subAccount == null) {
            Component errorMessage = reasonHolder.getMessageOrDefault(Component.translatable("error.numismatics.authorized_card.account_not_found"));
            throw new LuaException(errorMessage.getString());
        }


        return subAccount.getLabel();
    }

    @LuaFunction
    public final int getMaxAvailableWithdrawal(BankTerminalBlockEntity terminal, String accountID, String authorizationID) throws LuaException {
        UUID account$, authorization$;
        try {
            account$ = UUID.fromString(accountID);
            authorization$ = UUID.fromString(authorizationID);
        } catch (IllegalArgumentException e) {
            throw new LuaException("Invalid UUID");
        }

        Authorization authorization = new Authorization.Anonymous(authorization$);

        BankAccount bankAccount = Numismatics.BANK.getAccount(account$);
        if (bankAccount == null) {
            throw new LuaException("Account not found");
        }

        ReasonHolder reasonHolder = new ReasonHolder();
        SubAccount subAccount = bankAccount.getSubAccount(authorization, reasonHolder);

        if (subAccount == null) {
            Component errorMessage = reasonHolder.getMessageOrDefault(Component.translatable("error.numismatics.authorized_card.account_not_found"));
            throw new LuaException(errorMessage.getString());
        }

        IDeductable deductable = subAccount.getDeductor(authorization);

        if (deductable == null) {
            throw new LuaException("Deductor not found");
        }

        return deductable.getMaxWithdrawal();
    }

    @LuaFunction(mainThread = true)
    public final void transfer(BankTerminalBlockEntity terminal, String fromAccountID, String fromAuthorizationID, String toAccountID, int amount) throws LuaException {
        UUID from$, fromAuthorization$, to$;
        try {
            from$ = UUID.fromString(fromAccountID);
            fromAuthorization$ = UUID.fromString(fromAuthorizationID);
            to$ = UUID.fromString(toAccountID);
        } catch (IllegalArgumentException e) {
            throw new LuaException("Invalid UUID");
        }

        Authorization authorization = new Authorization.Anonymous(fromAuthorization$);

        BankAccount account = Numismatics.BANK.getAccount(from$);
        BankAccount toAccount = Numismatics.BANK.getAccount(to$);
        if (account == null || toAccount == null) {
            throw new LuaException("Account not found");
        }

        ReasonHolder reasonHolder = new ReasonHolder();
        SubAccount subAccount = account.getSubAccount(authorization, reasonHolder);

        if (subAccount == null) {
            Component errorMessage = reasonHolder.getMessageOrDefault(Component.translatable("error.numismatics.authorized_card.account_not_found"));
            throw new LuaException(errorMessage.getString());
        }

        IDeductable deductable = subAccount.getDeductor(authorization);

        if (deductable == null) {
            throw new LuaException("Deductor not found");
        }

        if (deductable.getMaxWithdrawal() < amount) {
            throw new LuaException("Insufficient funds");
        }

        if (deductable.deduct(amount, reasonHolder)) {
            toAccount.deposit(amount);
        } else {
            throw new LuaException(reasonHolder.getMessageOrDefault().getString());
        }
    }

    @Override
    public String id() {
        return "Numismatics_BankTerminal";
    }
}
