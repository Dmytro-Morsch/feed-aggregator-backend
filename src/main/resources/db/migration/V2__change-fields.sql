alter table feed
    rename column loaded to status;

alter table feed
    alter column status type varchar(20)
        using case
                  when status = true then 'DOWNLOADED'
                  when status = false then 'CREATED'
        end;